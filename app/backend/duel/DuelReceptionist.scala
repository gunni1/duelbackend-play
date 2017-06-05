package backend.duel

import backend.avatar.Avatar

/**
  * - Koordiniert Anfragen zu Duellen.
  * - Gibt das bestätigte Duell an den DuellManager weiter.
  * - Nur wenn beide Spieler einem Duell zustimmen wird es gestartet.
  */
class DuelReceptionist {
  def requestDuel(own: Avatar, other: Avatar)
}
