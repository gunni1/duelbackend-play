package backend.simulation

import backend.action.{Action, Attack, BlockedAttack, CounteredAttack}

import scala.util.Random

/**
  * Ein FightingAvatar führt eine Aktion auf einen anderen aus.
  */
class ActionExecution(executor: FightingAvatar, action: Action, random: Random){

  def on(opponent: FightingAvatar): ExecutionResult = {
    action match {
      case Attack() => performAttackOn(opponent)
      case BlockedAttack() => performAttackButBlockedOn(opponent)
      case CounteredAttack() => receiveCounterAttackFrom(opponent)
    }
  }

  private def performAttackOn(opponent: FightingAvatar) = {
    val randomDamage = gaussianDmg(executor)
    opponent.receiveDamage(randomDamage)
    AttackResult(executor, opponent, randomDamage)
  }

  private def receiveCounterAttackFrom(opponent: FightingAvatar) = {
    val randomDamage = gaussianDmg(opponent)
    executor.receiveDamage(randomDamage)
    CounterAttackResult(executor, opponent, randomDamage)
  }

  private def performAttackButBlockedOn(opponent: FightingAvatar) = {
    val randomDamage = gaussianDmg(executor)
    val damageAfterBlock = (randomDamage * 0.2).toInt
    opponent.receiveDamage(damageAfterBlock)
    BlockResult(executor, opponent, damageAfterBlock)
  }

  /**
    * Zufälliger Schaden nach Normalverteilung. Immer mindestens 1
    * - executor.strikeStrength: Erwartungswert
    * - executor.strikeAccuracy: Standardabweichung
    */
  private def gaussianDmg(executor: FightingAvatar): Int = {
    val gaussianDmg = math.floor(executor.strikeStrength + random.nextGaussian *
      executor.strikeAccuracy).toInt
    if (gaussianDmg < 1) 1 else gaussianDmg
  }
}
