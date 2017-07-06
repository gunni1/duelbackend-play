package backend.duel

import java.time.ZonedDateTime

import backend.avatar.persistence.AvatarId
import backend.duel.persistence.DuelId

/**
  * Ein durch den Spieler ausgelöster Befehl .
  */
trait UserCommand

case class Resign(avatarId: AvatarId, duelId: DuelId, issuedAt: ZonedDateTime)

