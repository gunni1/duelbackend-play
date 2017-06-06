package backend.duel

import backend.avatar.Avatar
import backend.duel.dto.{DuelRequestTimedOut, DuelStarted, RequestDuelResponse}
import backend.duel.persistence.DuelId

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.concurrent.duration._

/**
  * - Koordiniert Anfragen zu Duellen.
  * - Gibt das bestätigte Duell an den DuellManager weiter.
  * - Nur wenn beide Spieler einem Duell zustimmen wird es gestartet.
  */
class DuelReceptionist {
  private val openRequests: concurrent.TrieMap[(Avatar, Avatar), Promise[RequestDuelResponse]] =
    new TrieMap[(Avatar, Avatar), Promise[RequestDuelResponse]]()

  /**
    * Synchronisiert den Duellstart. Es wird ein wartender Request bestätigt oder auf die Bestätigung eines Requests
    * gewartet.
    *
    * @param own   Avatar des Request-Senders
    * @param other Herausgeforderter Avatar
    * @return
    */
  def requestDuel(own: Avatar, other: Avatar): RequestDuelResponse = {

    if(openRequests contains (other -> own)){
      //Dummy
      val duelId = DuelId("2")
      DuelStarted(duelId, 1)

      //Promise complete
      openRequests( other -> own).success(DuelStarted(duelId, 1))
      DuelStarted(duelId, 1)
    }
    else{
      val requestTupel = (own -> other)
      val p = Promise[RequestDuelResponse]

      openRequests += requestTupel -> p

      val future = p.future

      future.onComplete({
        case Success(duelStarted) => openRequests.remove((own -> other)); duelStarted
        case Failure(_) => DuelRequestTimedOut
      })
      Await.result(future, 30 seconds)
    }
  }
}
