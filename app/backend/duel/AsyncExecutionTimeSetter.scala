package backend.duel

import java.time.ZonedDateTime

import akka.actor.{Actor, Props}
import backend.duel.AsyncExecutionTimeSetter.SetNextExecutionTime
import backend.duel.persistence.DuelId

object AsyncExecutionTimeSetter {
  def props = Props[AsyncExecutionTimeSetter]
  case class SetNextExecutionTime(duelId: DuelId, zonedDateTime: ZonedDateTime)
}

/**
  * Actor-Instanz um asynchron Ausführungszeitpunkte im ActionExecutionTimeService zu speichern.
  */
class AsyncExecutionTimeSetter(actionExecutionTimeService: ActionExecutionTimeService) extends Actor {
  def receive: Receive = {
    case SetNextExecutionTime(duelId: DuelId, zonedDateTime: ZonedDateTime) => {
      actionExecutionTimeService.setNextExecution(duelId, zonedDateTime)
    }
  }
}
