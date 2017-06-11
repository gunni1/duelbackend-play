package backend.duel

import akka.actor.{ActorSystem, Props}
import backend.avatar.Avatar
import backend.duel.persistence.{DuelId, DuelRepository}
import backend.simulation.{DuelSimulator, FightingAvatar}
import backend.simulation.DuelSimulator.InitiateDuelBetween

import scala.util.Random

/**
  * - Koordiniert den Verlauf eines Duells
  * - Bedient Anfragen zu Aktionen während des Duells
  */
class DuelManager(duelRepository: DuelRepository, actorSystem: ActorSystem) {
  /**
    *
    * @param left  Herausfordernder Avatar
    * @param right Zum Duell Herausgeforderter Avatar
    * @return (duelId, intialReactionTime)
    *          Tupel aus Id des Duells und der Zeit bis zur ersten Aktion ab jetzt
    */
  def initiateDuel(left: Avatar, right: Avatar): (DuelId, Int) = {
    val duelId = duelRepository.nextDuelId
    val duelSimulator = actorSystem.actorOf(Props(new DuelSimulator(duelRepository)), duelId.asString)

    val fightsRandom = new Random()
    val fightingLeft = FightingAvatar(left, right, fightsRandom)
    val fightingRight = FightingAvatar(right, left, fightsRandom)

    val intialReactionTime = fightingLeft.actionTime.min(fightingRight.actionTime)

    duelSimulator ! InitiateDuelBetween(fightingLeft, fightingRight, duelId)

    (duelId, intialReactionTime)
  }
}
