package backend.simulation

import backend.action.Action

/**
  * Ergebnis einer Aktionsausführung
  */
trait ExecutionResult {
  def asString: String
  def executor: FightingAvatar
  def damageReceived: DamageReceived
}

case class DamageReceived(damagedAvatar: FightingAvatar, damage: Int) {
  def asString: String = damagedAvatar.name + " received " + damage + " damage."

}

case class AttackResult(executor: FightingAvatar, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.name + ". " + damageReceived.asString
}
case class BlockResult(executor: FightingAvatar, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.name + " was blocked. " + damageReceived.asString
}
case class CounterAttackResult(executor: FightingAvatar, damageReceived: DamageReceived) extends ExecutionResult {
  def asString = "Attack from: " + executor.name + " was countered." + damageReceived.asString
}