package backend.simulation.persistence

import backend.simulation.DuelProtocol
/**
  * Created by gunni on 09.05.17.
  */
trait DuelRepository {
  /**
    * Erzeugt die nächste verfügbare Duell-Id unter der ein Duell gespeichert werden kann
    */
  def nextDuelId: DuelId

  /**
    * Speichert ein DuelProtocol unter einer bestimmten DuelId. Wirft IllegalArgumentException
    * falls die DuelId bereits vergeben sein sollte.
    */
  def saveDuelProtocol(duelId: DuelId, duelProtocol: DuelProtocol): Unit

  /**
    * Liefert ein DuelProtocol anhand einer DuelId
    */
  def getDuelProtocol(duelId: DuelId): Option[DuelProtocol]
}
