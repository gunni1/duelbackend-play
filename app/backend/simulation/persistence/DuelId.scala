package backend.simulation.persistence

/**
  * Identifiziert über alle Komponenten ein Duell
  */
case class DuelId(id: String) {
  def asString = id.toString
}
