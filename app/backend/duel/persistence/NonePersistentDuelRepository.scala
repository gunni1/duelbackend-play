package backend.duel.persistence

import backend.duel.DuelEvent

import scala.collection.concurrent.TrieMap

/**
  * Speichert Duelle in einer Map identifiziert durch die DuelId
  */
class NonePersistentDuelRepository extends DuelRepository {
  private val duelEvents: TrieMap[DuelEventId, DuelEvent] = new TrieMap[DuelEventId, DuelEvent]()
  private val duelIdGenerator = DuelIdGenerator

  /**
    * Erzeugt die nächste verfügbare Duell-Id unter der ein Duell gespeichert werden kann
    * @return
    */
  def nextDuelId: DuelId = duelIdGenerator.nextId

  /**
    * Speichert ein Ereignis wärend eines Duells.
    */
  def saveDuelEvent(duelEventId: DuelEventId, duelEvent: DuelEvent): Unit =
    duelEvents.put(duelEventId, duelEvent)

  def loadDuelEvent(duelEventId: DuelEventId): Option[DuelEvent] = duelEvents.get(duelEventId)
}
