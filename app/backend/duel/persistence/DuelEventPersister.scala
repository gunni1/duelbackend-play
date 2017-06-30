package backend.duel.persistence

import akka.actor.{Actor, Props}
import backend.duel.DuelEvent
import backend.duel.persistence.DuelEventPersister.SaveDuelEvent
import com.google.inject.Inject

object DuelEventPersister {
  def props = Props[DuelEventPersister]
  case class SaveDuelEvent(duelEventId: DuelEventId, duelEvent: DuelEvent)
}

/**
  * Actor-Instanz zur asynchronen Persistierung von Duel-Events
  */
class DuelEventPersister @Inject() (duelRepository: DuelRepository) extends Actor {
  override def receive: Receive = {
    case SaveDuelEvent(duelEventId, duelEvent) => duelRepository.saveDuelEvent(duelEventId, duelEvent)
  }

}
