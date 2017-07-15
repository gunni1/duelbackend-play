package backend.duel

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import backend.avatar.Avatar
import backend.avatar.persistence.AvatarId
import backend.duel.DuelSimulator.{InitiateDuelBetween, IssueUserCommand}
import backend.duel.persistence.{DuelEventPersister, DuelEventRepository, DuelId}
import backend.simulation.FightingAvatar
import play.api.Logger

import scala.collection.concurrent.TrieMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Random, Success}

/**
  * - Koordiniert den Verlauf eines Duells
  * - Bedient Anfragen zu Aktionen während des Duells
  */
class DuelManager(duelEventRepository: DuelEventRepository, actorSystem: ActorSystem,
                  actionExecutionTimeService: ActionExecutionTimeService) {

  implicit val userCommandTimeout = Timeout(FiniteDuration(1, TimeUnit.SECONDS))
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

  def issueUserCommand(userCommand: UserCommand): Option[String] = {
    var errorMessage:Option[String] = None
    actorSystem.actorSelection("user/" + userCommand.duelId.asString).resolveOne().onComplete {
      case Success(duelSimulator) => duelSimulator ! IssueUserCommand(userCommand)
      case Failure(_) => errorMessage = Some("No actor found for duelId: "+ userCommand.duelId)
    }
    errorMessage
  }

}
