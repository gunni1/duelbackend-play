package backend.simulation

import akka.actor.{Actor, ActorSystem, Props}
import backend.avatar.Avatar
import backend.duel.persistence.{DuelId, DuelRepository}
import com.google.inject.Inject

import scala.util.Random

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
class DuelSimulator @Inject() (duelRepository: DuelRepository) extends Actor {
  import DuelSimulator._


  override def receive: Receive = {
    case InitiateDuelBetween(left: FightingAvatar, right: FightingAvatar, duelId: DuelId) => {
      val duelProtocol = simulateDuelBetween(left, right)
      duelRepository.saveDuelProtocol(duelId, duelProtocol)
    }
  }


  def simulateDuelBetween(left: FightingAvatar, right: FightingAvatar): DuelProtocol = {
    val duelControl = new DuelControl(left,right)

    duelControl.doNextActionUntilFinished
  }
}

