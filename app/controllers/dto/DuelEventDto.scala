package controllers.dto

import backend.avatar.persistence.AvatarId
import backend.duel.persistence.DuelEventId
import backend.simulation._

/**
  * Dient der Json-Serialisierung von DuelEvents.
  * Typ-Informationen können nicht direkt in JSON Umgewandelt werden.
  * In den DTOs werden Strings für Typen hinzugefügt.
  */
case class DuelEventDto (duelEventId: DuelEventId, duelEventType: String, executionResultDto: ExecutionResultDto)


case class ExecutionResultDto(action: String, executor: AvatarId, damageReceivedDto: DamageReceivedDto)

case class DamageReceivedDto(damagedAvatar: AvatarId, damage: Int)
