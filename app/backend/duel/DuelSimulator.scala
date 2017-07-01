package backend.duel

import akka.actor.{Actor, ActorRef, Props}
import backend.duel.persistence.DuelEventPersister.SaveDuelEvent
import backend.duel.persistence.{DuelEventId, DuelId}
import backend.simulation._
import controllers.dto.ExecutionResultDto

/**
  * Companion für Actor. Definiert zu empfangene "Events" als Case Class
  */
object DuelSimulator {
  def props = Props[DuelSimulator]
  case class InitiateDuelBetween(left: FightingAvatar, right: FightingAvatar, duelId: DuelId)
}

/**
  * Actor-Implementierung des Duel-Simulators.
  * Eine Actor-Instanz führt genau ein konkretes Duell aus
  */
class DuelSimulator (eventPersister: ActorRef) extends Actor {
  import DuelSimulator._

  override def receive: Receive = {
    case InitiateDuelBetween(left: FightingAvatar, right: FightingAvatar, duelId: DuelId) => {

      //Reaktionszeiten vergleichen, Wartezeit bestimmen, Ausführenden Avatar bestimmen
      val duelTimer = new DuelTimer(left, right)

      //TODO: Bessere Lösung finden!
      var actionCounter = 0

      while(isNotFinished(left,right))
      {
        val timerResult = duelTimer.next
        //Warten (Future)

        //Benutzeraktion?

        //Aktion ausführen
        val executing = timerResult.executing
        val executedOn = timerResult.executedOn
        val actionResult = executing.execute(left.nextAction).on(executedOn)

        //TODO: Bedingte Eventerzeugung: energie < 0 dann AvatarList
        //TODO: zeitpunkt der nächsten Aktion persistieren
        eventPersister ! SaveDuelEvent(ActionEvent(DuelEventId(duelId, actionCounter.toString), actionResult))

        actionCounter +=1
      }

    }
  }

  /**
    * Rekursive Funktion die Aktionen ausführt bis das Duell beendet ist.
    */
  private def executeNextAction(duelTimer: DuelTimer, duelId: DuelId, nextActionId: Int): DuelFinishedEvent = {
    val nextAction = duelTimer.next
    //Ausführungszeitpunkt

    //Warten bzw Ausführungszeitpunkt berechnen

    //existiert eine Benutzeraktion?
    //Ja -> ka
    //Nein -> Aktion ausführen + Persistieren
    //Existiert ein gewinner?
      //ja ->   Finished-Event
      //nein -> Rekursiv aufrufen


    val executing = nextAction.executing
    val executedOn = nextAction.executedOn


    val executionResult = executing.execute(executing.nextAction).on(executedOn)


    executionResult.


  }

  private def executeNextAction

  private def isNotFinished(left: FightingAvatar,right: FightingAvatar): Boolean =
    left.actualEnergy > 0 && right.actualEnergy > 0

}

