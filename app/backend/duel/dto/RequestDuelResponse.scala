package backend.duel.dto

import backend.duel.persistence.DuelId

/**
  * Antwort-DTO für eine Duell-Herausforderung
  */
trait RequestDuelResponse
case class DuelStarted(duelId: DuelId, duelStartTime: ZonedDateTime, int )
