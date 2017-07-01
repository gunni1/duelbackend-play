package backend.duel

import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoField, ChronoUnit, TemporalUnit}
import java.time.{ZoneId, ZonedDateTime}

import backend.avatar.Avatar
import backend.duel.dto.{DuelRequestTimedOut, DuelStarted}
import backend.duel.persistence.DuelEventRepository
import controllers.dto.{DuelRequestTimedOut, DuelStarted, RequestDuelResponse}

import scala.collection.concurrent.TrieMap
import scala.concurrent.{Await, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._
import play.api.Logger


/**
  * - Koordiniert Anfragen zu Duellen.
  * - Gibt das bestätigte Duell an den DuellManager weiter.
  * - Nur wenn beide Spieler einem Duell zustimmen wird es gestartet.
  */
class DuelReceptionist(duelRepository: DuelEventRepository, duelManager: DuelManager) {

  private val openRequests: TrieMap[(Avatar, Avatar), Promise[RequestDuelResponse]] =
    new TrieMap[(Avatar, Avatar), Promise[RequestDuelResponse]]()

  def DATE_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME

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
      Logger.info("Request: " + own.name + " -> " + other.name + ". Open request found. completing promise")

      //Berechnung Außerhalb des DuelSimulator ok, da es sich um einen Sonderfall handelt?
      val (duelId, initialReactionTimeMillis) = duelManager.initiateDuel(other, own)

      val firstActionTime = ZonedDateTime.now(ZoneId.systemDefault()).plus(initialReactionTimeMillis, ChronoUnit.MILLIS)
      val dto = DuelStarted(duelId, firstActionTime.format(DATE_FORMAT))

      openRequests(other -> own).success(dto)
      dto
    }
    else {
      //1. Request: Herausfordernder Spieler
      Logger.info("Request: " + own.name + " -> " + other.name + ". Waiting for another Request to complete")
      val responsePromise = Promise[RequestDuelResponse]

      openRequests += (own -> other) -> responsePromise

      val response = Try(Await.result(responsePromise.future, 30 seconds)) match {
        case Success(duelStarted) => duelStarted
        case Failure(_) => DuelRequestTimedOut()
      }
      openRequests.remove((own -> other))
      response
    }
  }
}
