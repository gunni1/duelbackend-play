package backend.duel

import akka.actor.{ActorSystem, Props}
import backend.avatar.Avatar
import backend.duel.DuelSimulator.InitiateDuelBetween
import backend.duel.persistence.{DuelEventPersister, DuelId, DuelEventRepository}
import backend.simulation.FightingAvatar

import scala.util.Random

/**
  * - Koordiniert den Verlauf eines Duells
  * - Bedient Anfragen zu Aktionen während des Duells
  */
class DuelManager(duelEventRepository: DuelEventRepository, actorSystem: ActorSystem,
                  actionExecutionTimeService: ActionExecutionTimeService) {
  /**
    *
    * @param left  Herausfordernder Avatar
    * @param right Zum Duell Herausgeforderter Avatar
    * @return (duelId, intialReactionTime)
    *          Tupel aus Id des Duells und der Zeit bis zur ersten Aktion ab jetzt
    */
  def initiateDuel(left: Avatar, right: Avatar): (DuelId, Int) = {
    val duelId = duelEventRepository.nextDuelId

    val duelPersister = actorSystem.actorOf(Props(new DuelEventPersister(duelEventRepository)))
    val execTimeSetter = actorSystem.actorOf(Props(new AsyncExecutionTimeSetter(actionExecutionTimeService)))
    val duelSimulator = actorSystem.actorOf(
      Props(new DuelSimulator(duelPersister, execTimeSetter, duelId)), duelId.asString)

    val fightsRandom = new Random()
    val fightingLeft = FightingAvatar(left, right, fightsRandom)
    val fightingRight = FightingAvatar(right, left, fightsRandom)

    val intialReactionTime = fightingLeft.actionTime.min(fightingRight.actionTime)

    duelSimulator ! InitiateDuelBetween(fightingLeft, fightingRight)

    (duelId, intialReactionTime)
  }
}
