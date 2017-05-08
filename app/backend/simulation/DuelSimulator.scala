package backend.simulation

import akka.actor.{Actor, Props}
import backend.avatar.Avatar
import backend.simulation.persistence.DuelId

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
class DuelSimulator extends Actor {
  import DuelSimulator._

  override def receive: Receive = {
    case InitiateDuelBetween(leftAvatar: Avatar, rightAvatar: Avatar, duelId: DuelId) => {

      val duelProtocol = simulateDuelBetween(leftAvatar, rightAvatar)
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

