package backend.duel

import java.time.temporal.ChronoUnit
import java.time.{ZoneId, ZonedDateTime}

import akka.actor.{Actor, ActorRef, Props}
import backend.avatar.persistence.AvatarId
import backend.duel.AsyncExecutionTimeSetter.SetNextExecutionTime
import backend.duel.persistence.DuelEventPersister.SaveDuelEvent
import backend.duel.persistence.{DuelEventId, DuelId}
import backend.simulation._

import scala.collection.concurrent._

/**
  * Companion für Actor. Definiert zu empfangene "Events" als Case Class
  */
object DuelSimulator {
  def props = Props[DuelSimulator]
  case class InitiateDuelBetween(left: FightingAvatar, right: FightingAvatar)
  case class IssueUserCommand(userCommand: UserCommand)
}

/**
  * Actor-Implementierung des Duel-Simulators.
  * Eine Actor-Instanz führt genau ein konkretes Duell aus
  */
class DuelSimulator (eventPersister: ActorRef, execTimeSetter: ActorRef, duelId: DuelId) extends Actor {
  import DuelSimulator._

  private val userCommands: TrieMap[AvatarId, UserCommand] = new TrieMap[AvatarId, UserCommand]()

  override def receive: Receive = {
    case InitiateDuelBetween(left: FightingAvatar, right: FightingAvatar) => {

      //Reaktionszeiten vergleichen, Wartezeit bestimmen, Ausführenden Avatar bestimmen
      val duelTimer = new DuelTimer(left, right)
      val duelFinishedEvent = executeNextAction(duelTimer, duelId, 0)
      eventPersister ! SaveDuelEvent (duelFinishedEvent)

      //ggf Avatar entfernen veranlassen
    }
    case IssueUserCommand(userCommand: UserCommand) => {
      if(userCommand.duelId.equals(duelId))
      {
        userCommands.put(userCommand.avatarId, userCommand)
      }
    }
  }

  /**
    * Rekursive Funktion die Aktionen ausführt bis das Duell beendet ist.
    */
  private def executeNextAction(duelTimer: DuelTimer, duelId: DuelId, nextActionId: Int): DuelFinishedEvent = {
    val nextAction = duelTimer.next
    val executing = nextAction.executing
    val executedOn = nextAction.executedOn
    execTimeSetter ! SetNextExecutionTime(duelId, calcNextExecTime(nextAction.nextActionIn))

    //Warten
    Thread.sleep(30000)

    //hat ein Spieler aufgegeben?
    resignCommandIssued(executing, executedOn) match {
      case Some(Resign(avatarId,_,_)) => return Resigned(DuelEventId(duelId,nextActionId.toString), avatarId)
    }


    //Aktion Ausführen
    val executionResult = executing.execute(executing.nextAction).on(executedOn)

    if (executionResult.damageReceived.damagedAvatar.actualEnergy <= 0){
      return AvatarLose(DuelEventId(duelId,nextActionId.toString), executionResult)
    }
    else {
      eventPersister ! SaveDuelEvent(ActionEvent(DuelEventId(duelId,nextActionId.toString), executionResult))

      executeNextAction(duelTimer, duelId, nextActionId + 1)
    }
  }

  /**
    * Ermittelt, ob das Duellvom Spieler aufgegeben wurde.
    * Wurde von beiden Spielern aufgegeben, wird die zuerst ausgeführte Aufgabe berücksichtigt.
    */
  def resignCommandIssued(executing: FightingAvatar, executedOn: FightingAvatar): Option[Resign] = {
    (userCommands.get(executing.avatarId), userCommands.get(executedOn.avatarId)) match {
      case (Some(x: Resign),Some(y: Resign)) => if (x.issuedAt.isBefore(y.issuedAt)) Some(x) else Some(y)
      case (Some(x: Resign), None) => Some(x)
      case (None, Some(y: Resign)) => Some(y)
      case (_, _) => None
    }
  }

  def calcNextExecTime(nextActionIn: Int): ZonedDateTime =
    ZonedDateTime.now(ZoneId.systemDefault()).plus(nextActionIn, ChronoUnit.MILLIS)
}

