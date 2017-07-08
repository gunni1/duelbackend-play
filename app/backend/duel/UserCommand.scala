package backend.duel

import java.time.ZonedDateTime

import backend.avatar.persistence.AvatarId
import backend.duel.persistence.DuelId

/**
  * Ein durch den Spieler ausgelöster Befehl .
  */
trait UserCommand {
  def avatarId: AvatarId
  def duelId: DuelId
}

case class Resign(avatarId: AvatarId, duelId: DuelId, issuedAt: ZonedDateTime)

