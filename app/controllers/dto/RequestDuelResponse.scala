package controllers.dto

import backend.duel.persistence.DuelId

/**
  * Antwort-DTO für eine Duell-Herausforderung
  */
trait RequestDuelResponse
case class DuelStarted(duelId: DuelId, firstActionTime: String) extends RequestDuelResponse
case class DuelRequestTimedOut() extends RequestDuelResponse
