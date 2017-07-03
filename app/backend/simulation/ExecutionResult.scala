package backend.simulation

import backend.avatar.persistence.AvatarId

/**
  * Ergebnis einer Aktionsausführung
  */
trait ExecutionResult {
  def asString: String
  def executor: FightingAvatar
  def damageReceived: DamageReceived

}

case class DamageReceived(damagedAvatar: FightingAvatar, damage: Int) {
  def asString: String = damagedAvatar.avatarId.id + " received " + damage + " damage."

}

case class AttackResult(executor: FightingAvatar, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.avatarId.id + ". " + damageReceived.asString
}
case class BlockResult(executor: FightingAvatar, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.avatarId.id + " was blocked. " + damageReceived.asString
}
case class CounterAttackResult(executor: FightingAvatar, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.avatarId.id + " was countered." + damageReceived.asString
}