package backend.duel.persistence

import backend.avatar.persistence.AvatarId

/**
  * Ein Ereigniss wärend eines Duells. Ereignisse werden im DuelProtocol festgehalten.
  */
trait DuelEvent
case class ActionEvent() extends DuelEvent
case class AvatarLost() extends DuelEvent
case class ThrownInTheTowel(avatarId: AvatarId) extends DuelEvent
