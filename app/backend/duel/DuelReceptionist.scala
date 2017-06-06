package backend.duel

import backend.avatar.Avatar
import backend.avatar.persistence.AvatarId
import backend.duel.dto.RequestDuelResponse
import backend.duel.persistence.DuelId

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
    * @param own   Avatar des Request senders
    * @param other Herausgeforderter Avatar
    * @return
    */
  def requestDuel(own: Avatar, other: Avatar): RequestDuelResponse = {
    ???
  }
}
