package backend.duel


import java.time.ZonedDateTime

import backend.duel.persistence.DuelId

import scala.collection.concurrent.TrieMap

/**
  * Speichert den Zeitpunkt wann die nächste Aktion ausgeführt wird.
  */
class ActionExecutionTimeService {
  private val executionTimesStamps: TrieMap[DuelId, ZonedDateTime] = new TrieMap[DuelId, ZonedDateTime]()

  def setNextExecution(duelId: DuelId, zonedDateTime: ZonedDateTime) =
    executionTimesStamps.put(duelId, zonedDateTime)

  def getNextExecution(duelId: DuelId): Option[ZonedDateTime] = executionTimesStamps.get(duelId)

  //TODO: Regelmäßiges löschen von nichtmehr benötigten Einträgen
}
