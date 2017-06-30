package backend.duel.persistence

import backend.duel.DuelEvent
/**
  * Created by gunni on 09.05.17.
  */
trait DuelRepository {
  /**
    * Erzeugt die nächste verfügbare Duell-Id unter der ein Duell gespeichert werden kann
    */
  def nextDuelId: DuelId

  /**
    * Speichert ein Ereignis wärend eines Duells.
    */
  def saveDuelEvent(duelEventId: DuelEventId, duelEvent: DuelEvent): Unit

  def loadDuelEvent(duelEventId: DuelEventId): Option[DuelEvent]
}
