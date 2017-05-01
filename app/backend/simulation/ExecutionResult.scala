package backend.simulation

/**
  * Ergebnis einer Aktionsausführung
  */
trait ExecutionResult{
  def asString: String
}


case class AttackResult(executor: FightingAvatar, opponent: FightingAvatar, damage: Int) extends ExecutionResult {
  override def asString: String =
    executor.name + " executed attack on " + opponent.name + " for " + damage + " damage." +
      "(" + opponent.actualEnergy + "/100)"
}
case class BlockResult(executor: FightingAvatar, opponent: FightingAvatar, damage: Int) extends ExecutionResult {
  override def asString: String =
    "Attack from " + executor.name + " on " + opponent.name + " was blocked. " + opponent.name + " received " + damage +
      " damage." + "(" + opponent.actualEnergy + "/100)"
}
case class CounterAttackResult(originalAttacker: FightingAvatar, counterAttacker: FightingAvatar, damage: Int) extends ExecutionResult {
  override def asString: String =
    originalAttacker.name + " attacked but " + counterAttacker.name + " parries and counter attack " + originalAttacker.name +
      " for " + damage + " damage." + "(" + originalAttacker.actualEnergy + "/100)"
}

