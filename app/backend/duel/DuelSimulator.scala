package backend.duel

import java.time.temporal.ChronoUnit
import java.time.{ZoneId, ZonedDateTime}

import akka.actor.{Actor, ActorRef, Props}
import backend.avatar.persistence.AvatarId
import backend.duel.AsyncExecutionTimeSetter.SetNextExecutionTime
import backend.duel.persistence.DuelEventPersister.SaveDuelEvent
import backend.duel.persistence.{DuelEventId, DuelId}
import backend.simulation._
import play.api.Logger

import scala.concurrent.duration._
import scala.collection.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Companion für Actor. Definiert zu empfangene "Events" als Case Class
  */
object DuelSimulator {
  def props = Props[DuelSimulator]
  case class InitiateDuelBetween(left: FightingAvatar, right: FightingAvatar)
  case class IssueUserCommand(userCommand: UserCommand)
  case class ExecuteNextAction(duelTimer: DuelTimer, nextAction: TimerResult, duelId: DuelId, nextActionId: Int)
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

      val duelTimer = new DuelTimer(left, right)

      val nextAction = duelTimer.next
      execTimeSetter ! SetNextExecutionTime(duelId, calcNextExecTime(nextAction.nextActionIn))

      context.system.scheduler.scheduleOnce(
        nextAction.nextActionIn.millis, self, ExecuteNextAction(duelTimer, nextAction, duelId, 0))
    }
    case IssueUserCommand(userCommand: UserCommand) => {
      if(userCommand.duelId.equals(duelId)) {
        userCommands.put(userCommand.avatarId, userCommand)
      }
    }
    case ExecuteNextAction(duelTimer: DuelTimer, actualAction: TimerResult, duelId: DuelId, actualActionId: Int) => {
      val executing = actualAction.executing
      val executedOn = actualAction.executedOn

      val maybeResigned = resignCommandIssued(executing, executedOn)

      if(maybeResigned.isDefined)
      {
        eventPersister ! SaveDuelEvent(Resigned(DuelEventId(duelId,actualActionId.toString), maybeResigned.get.avatarId))
      }
      else
      {
        val executionResult = executing.execute(executing.nextAction).on(executedOn)

        if (executionResult.damageReceived.damagedAvatar.actualEnergy <= 0)
        {
          eventPersister ! SaveDuelEvent(AvatarLose(DuelEventId(duelId,actualActionId.toString), executionResult))
        }
        else
        {
          eventPersister ! SaveDuelEvent(ActionEvent(DuelEventId(duelId,actualActionId.toString), executionResult))

          val nextAction = duelTimer.next
          execTimeSetter ! SetNextExecutionTime(duelId, calcNextExecTime(nextAction.nextActionIn))
          Logger.info("sceduling action in " + actualAction.nextActionIn.millis)
          context.system.scheduler.scheduleOnce(
            actualAction.nextActionIn.millis, self, ExecuteNextAction(duelTimer, nextAction, duelId, actualActionId + 1))
        }
      }
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

