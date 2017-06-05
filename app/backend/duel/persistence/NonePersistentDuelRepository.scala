package backend.duel.persistence

import backend.simulation.DuelProtocol

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap

/**
  * Speichert Duelle in einer Map identifiziert durch die DuelId
  */
class NonePersistentDuelRepository extends DuelRepository {
  private val duels: concurrent.TrieMap[DuelId, DuelProtocol] = new TrieMap[DuelId, DuelProtocol]()
  private val idGenerator = DuelIdGenerator

  /**
    * Erzeugt die nächste verfügbare Duell-Id unter der ein Duell gespeichert werden kann
    * @return
    */
  def nextDuelId: DuelId = idGenerator.nextId

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
}
