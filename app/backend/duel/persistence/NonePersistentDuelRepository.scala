package backend.duel.persistence

import backend.duel.DuelEvent
import backend.simulation.DuelProtocol

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap

/**
  * Speichert Duelle in einer Map identifiziert durch die DuelId
  */
class NonePersistentDuelRepository extends DuelRepository {
  private val duels: concurrent.TrieMap[DuelId, DuelProtocol] = new TrieMap[DuelId, DuelProtocol]()
  private val duelEvents: TrieMap[DuelEventId, DuelEvent] = new TrieMap[DuelEventId, DuelEvent]()
  private val duelIdGenerator = DuelIdGenerator

  /**
    * Erzeugt die nächste verfügbare Duell-Id unter der ein Duell gespeichert werden kann
    * @return
    */
  def nextDuelId: DuelId = duelIdGenerator.nextId

  /**
    * Speichert ein DuelProtocol unter einer bestimmten DuelId. Wirft IllegalArgumentException
    * falls die DuelId bereits vergeben sein sollte.
    */
  def saveDuelProtocol(duelId: DuelId, duelProtocol: DuelProtocol): Unit = {
    if(duels.get(duelId).isDefined){
      throw new IllegalArgumentException("duel with id: " + duelId + "already exist.")
    }
    else{
      duels.put(duelId, duelProtocol)
    }
  }

  def getDuelProtocol(duelId: DuelId) = duels.get(duelId)

  /**
    * Speichert ein Ereignis wärend eines Duells.
    */
  def saveEvent(duelEventId: DuelEventId, duelEvent: DuelEvent): Unit =
    duelEvents.put(duelEventId, duelEvent)
}
