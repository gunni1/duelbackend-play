package backend.duel

import backend.avatar.persistence.AvatarId
/**
  * Repräsentiert ein Ereignis in einem Duell:  Jedes Event besitzt
  *   - DuelId
  *   - EventId (fortlaufend wärend des Duells)
  */
trait DuelEvent
case class ActionEvent(executor: AvatarId, executedOn: AvatarId) extends DuelEvent
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
