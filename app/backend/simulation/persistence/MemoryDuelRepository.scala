package backend.simulation.persistence

import backend.simulation.DuelProtocol
import scala.collection.concurrent
import scala.collection.concurrent.TrieMap

/**
  * Speichert Duelle in einer Map identifiziert durch die DuelId
  */
object MemoryDuelRepository extends DuelRepository {
  private val duels: concurrent.TrieMap[DuelId, DuelProtocol] = new TrieMap[DuelId, DuelProtocol]()
  private val idGenerator = new DuelIdGenerator


}
