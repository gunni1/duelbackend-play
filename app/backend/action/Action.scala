package backend.action

trait Action
case class Attack() extends Action
case class BlockedAttack() extends Action
case class CounteredAttack() extends Action
