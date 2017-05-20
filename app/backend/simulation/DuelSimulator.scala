package backend.simulation

import akka.actor.{Actor, ActorSystem, Props}
import backend.avatar.Avatar
import backend.simulation.persistence.{DuelId, DuelRepository}
import com.google.inject.Inject

import scala.util.Random

/**
  * Companion für Actor. Definiert zu empfangene "Events" als Case Class
  */
object DuelSimulator {
  def props = Props[DuelSimulator]
  case class InitiateDuelBetween(leftAvatar: Avatar, rightAvatar: Avatar, duelId: DuelId)
}

/**
  * Actor-Implementierung des Duel-Simulators
  */
class DuelSimulator @Inject() (duelRepository: DuelRepository) extends Actor {
  import DuelSimulator._

  override def receive: Receive = {
    case InitiateDuelBetween(leftAvatar: Avatar, rightAvatar: Avatar, duelId: DuelId) => {

      val duelProtocol = simulateDuelBetween(leftAvatar, rightAvatar)
      duelRepository.saveDuelProtocol(duelId, duelProtocol)
    }
  }

  def simulateDuelBetween(leftAvatar: Avatar, rightAvatar: Avatar): DuelProtocol = {
    val fightsRandom = new Random()
    val left = FightingAvatar(leftAvatar, rightAvatar,fightsRandom)
    val right = FightingAvatar(rightAvatar, leftAvatar,fightsRandom)

    val duelControl = new DuelControl(left,right)

    duelControl.doNextActionUntilFinished
  }
}

