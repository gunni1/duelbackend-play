package backend.duel

import backend.avatar.persistence.AvatarId
import backend.duel.persistence.DuelEventId
import backend.simulation.ExecutionResult
/**
  * Repräsentiert ein Ereignis in einem Duell
  */
trait DuelEvent {
  def eventId : DuelEventId
  def asString : String
}


case class ActionEvent(duelEventId: DuelEventId, executionResult: ExecutionResult) extends DuelEvent {
  def eventId = duelEventId
  def asString = "Event: " + duelEventId.asString + " " + executionResult.asString
}

trait DuelFinishedEvent extends DuelEvent
/**
  * Das Duell ist beendet.
  * Ein Avatar hat regulär verloren und kann kein weiteres Duell bestreiten.
  */
case class AvatarLose(duelEventId: DuelEventId, executionResult: ExecutionResult) extends DuelFinishedEvent {
  def eventId = duelEventId
  def asString = "Duel Finished: " + duelEventId.asString + " " + executionResult.asString +
    " Avatar Lost."
}

/**
  * Das Duell ist beendet. Ein Spieler hat aufgegeben und der
  * unterlegene Avatar bleibt existent.
  */
case class Resigned(duelEventId: DuelEventId, avatarId: AvatarId) extends DuelFinishedEvent{
  def eventId = duelEventId
  def asString = "Duel Finished: " + duelEventId.asString + " AvatarId: " +
    avatarId.id + " resigned."
}
