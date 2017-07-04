package controllers.dto

import backend.duel.{ActionEvent, AvatarLose, DuelEvent}
import backend.simulation.{AttackResult, BlockResult, CounterAttackResult, ExecutionResult}

/**
  * Erzeugt aus DuelEvents Objekte für den Datenaustausch.
  */
object DuelEventDtoFactory {
  def toDto(executionResult: ExecutionResult): ExecutionResultDto ={
    val action = executionResult match {
      case AttackResult(_, _) => "Attack"
      case BlockResult(_,_) => "BlockedAttack"
      case CounterAttackResult(_,_) => "CounteredAttack"
    }

    ExecutionResultDto(action, executionResult.executor.avatarId,
      DamageReceivedDto(executionResult.damageReceived.damagedAvatar.avatarId, executionResult.damageReceived.damage))
  }
}
