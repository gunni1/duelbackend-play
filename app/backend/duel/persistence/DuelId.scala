package backend.duel.persistence

/**
  * Identifiziert über alle Komponenten ein Duell
  */
case class DuelId(id: String) {
  def asString = id.toString
}

/**
  * Identifiziert ein DuelEvent. Ein Duell-Event bezieht sich immer auf ein Duel (DuelId).
  */
case class DuelEventId(duelId: DuelId, eventId: String)
