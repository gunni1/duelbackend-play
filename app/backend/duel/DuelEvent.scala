package backend.duel

import backend.avatar.persistence.AvatarId
import backend.duel.persistence.DuelEventId
import backend.simulation.ExecutionResult
/**
  * Repräsentiert ein Ereignis in einem Duell
  */
trait DuelEvent
case class ActionEvent(duelIdEventId: DuelEventId, executionResult: ExecutionResult) extends DuelEvent
trait DuelFinishedEvent extends DuelEvent
/**
  * Das Duell ist beendet. Ein Avatar hat regulär verloren und wird entfernt.
  */
case class AvatarLost(avatarId: AvatarId) extends DuelFinishedEvent

/**
  * Das Duell ist beendet. Ein Spieler hat aufgegeben und der
  * unterlegene Avatar bleibt existent.
  */
case class ThrownInTheTowel(avatarId: AvatarId) extends DuelFinishedEvent
