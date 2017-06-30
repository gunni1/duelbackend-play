package backend.duel

import akka.actor.{Actor, ActorRef, Props}
import backend.duel.persistence.{DuelEventId, DuelId}
import backend.simulation._

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
class DuelSimulator (duelPersister: ActorRef) extends Actor {
  import DuelSimulator._

  override def receive: Receive = {
    case InitiateDuelBetween(left: FightingAvatar, right: FightingAvatar, duelId: DuelId) => {

      //Reaktionszeiten vergleichen, Wartezeit bestimmen, Ausführenden Avatar bestimmen
      val duelTimer = new DuelTimer(left, right)

      //TODO: Bessere Lösung finden!
      var actionCounter = 0;

      while(isNotFinished(left,right))
      {
        val timerResult = duelTimer.next
        //Warten (Future)

        //Benutzeraktion?

        //Aktion ausführen
        val executing = timerResult.executing
        val executedOn = timerResult.executedOn
        val actionResult = executing.execute(left.nextAction).on(executedOn)
        ActionEvent(DuelEventId(duelId, actionCounter.toString), actionResult)
        //TODO: Was kommt alles in das DuelResult was im Duell-Event gespeichert werden muss?

        actionCounter +=1;
      }
    }
  }

  private def isNotFinished(left: FightingAvatar,right: FightingAvatar): Boolean =
    left.actualEnergy > 0 && right.actualEnergy > 0

}

