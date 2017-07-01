package backend.simulation

import backend.avatar.persistence.AvatarId

/**
  * Ergebnis einer Aktionsausführung
  */
trait ExecutionResult {
  def asString: String

}

case class DamageReceived(damagedAvatar: AvatarId, damage: Int) {
  def asString: String = damagedAvatar.id + " received " + damage + " damage."

}

case class AttackResult(executor: AvatarId, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.id + ". " + damageReceived.asString
}
case class BlockResult(executor: AvatarId, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.id + " was blocked. " + damageReceived.asString
}
case class CounterAttackResult(executor: AvatarId, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.id + " was countered." + damageReceived.asString
}