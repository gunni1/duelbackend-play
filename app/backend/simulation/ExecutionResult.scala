package backend.simulation

import backend.avatar.persistence.AvatarId

/**
  * Ergebnis einer Aktionsausführung
  */
trait ExecutionResult

case class DamageReceived(damagedAvatar: AvatarId, damage: Int)

case class AttackResult(executor: AvatarId, damageReceived: DamageReceived) extends ExecutionResult
case class BlockResult(executor: AvatarId, damageReceived: DamageReceived) extends ExecutionResult
case class CounterAttackResult(executor: AvatarId, damageReceived: DamageReceived) extends ExecutionResult