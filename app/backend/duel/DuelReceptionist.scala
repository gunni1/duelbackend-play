package backend.duel

import akka.actor.{ActorSystem, Props}
import backend.avatar.Avatar
import backend.duel.dto.{DuelRequestTimedOut, DuelStarted, RequestDuelResponse}
import backend.duel.persistence.{DuelId, DuelRepository}
import backend.simulation.DuelSimulator
import backend.simulation.DuelSimulator.InitiateDuelBetween

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._
import play.api.Logger



/**
  * - Koordiniert Anfragen zu Duellen.
  * - Gibt das bestätigte Duell an den DuellManager weiter.
  * - Nur wenn beide Spieler einem Duell zustimmen wird es gestartet.
  */
class DuelReceptionist(duelRepository: DuelRepository, actorSystem: ActorSystem) {
  private val openRequests: concurrent.TrieMap[(Avatar, Avatar), Promise[RequestDuelResponse]] =
    new TrieMap[(Avatar, Avatar), Promise[RequestDuelResponse]]()

  /**
    * Synchronisiert den Duellstart. Es wird ein wartender Request bestätigt oder auf die Bestätigung
    * eines Requests gewartet.
    *
    * @param own   Avatar des Request-Senders
    * @param other Herausgeforderter Avatar
    * @return
    */
  def requestDuel(own: Avatar, other: Avatar): RequestDuelResponse = {

    if (openRequests contains (other -> own)) {
      //2. Request: Herausgeforderter Spieler
      Logger.info("Request: "+ own.name + " -> " + other.name + ". Open request found. completing promise")
      val duelId = duelRepository.nextDuelId
      val duelSimulator = actorSystem.actorOf(Props(new DuelSimulator(duelRepository)), duelId.asString)

      duelSimulator ! InitiateDuelBetween(other, own, duelId)

      openRequests(other -> own).success(DuelStarted(duelId, 1))
      DuelStarted(duelId, 1)
    }
    else {
      //1. Request: Herausfordernder Spieler
      Logger.info("Request: "+ own.name + " -> " + other.name + ". Waiting for another Request to complete")
      val responsePromise = Promise[RequestDuelResponse]

      openRequests += (own -> other) -> responsePromise

      val response= Try(Await.result(responsePromise.future, 30 seconds)) match{
        case Success(duelStarted) => duelStarted
        case Failure(_) => DuelRequestTimedOut()
      }
      openRequests.remove((own -> other));
      response
    }
  }
}
