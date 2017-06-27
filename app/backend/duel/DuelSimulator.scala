package backend.duel

import akka.actor.{Actor, ActorRef, Props}
import backend.duel.persistence.DuelId
import backend.simulation._
import org.scalatest.fixture

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
      val duelProtocol = simulateDuelBetween(left, right)

      //Reaktionszeiten vergleichen, Wartezeit bestimmen, Ausführenden Avatar bestimmen
      val duelTimer = new DuelTimer(left, right)
      while(isNotFinished(left,right))
      {
        val timerResult = duelTimer.next
        //Warten (Future)

        //Benutzeraktion?

        //Aktion ausführen
        val executing = timerResult.executing
        val executedOn = timerResult.executedOn
        val actionResult = executing.execute(left.nextAction).on(executedOn)
        ActionEvent()
        //TODO: Was kommt alles in das DuelResult was im Duell-Event gespeichert werden muss?
      }
    }
  }

  def simulateDuelBetween(left: FightingAvatar, right:FightingAvatar) = {
    val protocol = new DuelProtocolBuilder
    val duelTimer = new DuelTimer(left, right)

    while(isNotFinished(left, right))
    {
      duelTimer.next match {
        case TimerResult(waited, next) if next.equals(left) => {
          protocol.logWait(waited)
          protocol.logAction(left.execute(left.nextAction).on(right))
        }
        case TimerResult(waited, next) if next.equals(right) => {
          protocol.logWait(waited)
          protocol.logAction(right.execute(right.nextAction).on(left))
        }
      }
    }

  }

  private def isNotFinished(left: FightingAvatar,right: FightingAvatar): Boolean =
    left.actualEnergy > 0 && right.actualEnergy > 0

}

