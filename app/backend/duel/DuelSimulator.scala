package backend.duel

import akka.actor.{Actor, ActorRef, Props}
import backend.duel.persistence.DuelId
import backend.simulation.{DuelControl, DuelProtocol, FightingAvatar}

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


    }
  }

  def simulateDuelBetween(left: FightingAvatar, right: FightingAvatar): DuelProtocol = {

    new DuelControl(left, right).doNextActionUntilFinished
  }
}

