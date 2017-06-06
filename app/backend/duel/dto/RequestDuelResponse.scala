package backend.duel.dto

import java.time.ZonedDateTime

import backend.duel.persistence.DuelId

/**
  * Antwort-DTO für eine Duell-Herausforderung
  */
trait RequestDuelResponse
case class DuelStarted(duelId: DuelId, reactionTimeMillis: Int) extends RequestDuelResponse
case class DuelRequestTimedOut() extends RequestDuelResponse
