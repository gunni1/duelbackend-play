package controllers.dto

import backend.duel.persistence.DuelEventId
import backend.simulation.DamageReceived

/**
  * Dient der Json-Serialisierung von DuelEvents.
  * Typ-Informationen können nicht direkt in JSON Umgewandelt werden.
  * In den DTOs werden Strings für Typen hinzugefügt.
  */
case class DuelEventDto (duelEventId: DuelEventId, duelEventType: String)

case class ExecutionResultDto(action: String, damageReceived: DamageReceived)
