package backend.duel.persistence

/**
  * Repräsentiert ein Duell-Protocol aus sicht eines externen Systems.
  */
case class DuelProtocolModel(duelId: String, duelLog: List[String], winner: String)
