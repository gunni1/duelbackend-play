package backend.duel

import backend.avatar.Avatar
import backend.duel.dto.RequestDuelResponse

import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * - Koordiniert Anfragen zu Duellen.
  * - Gibt das bestätigte Duell an den DuellManager weiter.
  * - Nur wenn beide Spieler einem Duell zustimmen wird es gestartet.
  */
class DuelReceptionist {

  /**
    * Synchronisiert den Duellstart. Es wird ein wartender Request bestätigt oder auf die Bestätigung eines Requests
    * gewartet.
    *
    * @param own   Avatar des Request-Senders
    * @param other Herausgeforderter Avatar
    * @return
    */
  def requestDuel(own: Avatar, other: Avatar): RequestDuelResponse = {

    val response = Future {
      Thread.sleep(10000)
    }




    ???
  }

}
