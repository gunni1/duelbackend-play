package backend.duel.persistence

import akka.actor.{Actor, Props}
import backend.duel.DuelEvent
import backend.duel.persistence.DuelEventPersister.SaveDuelEvent
import com.google.inject.Inject
import play.api.Logger

object DuelEventPersister {
  def props = Props[DuelEventPersister]
  case class SaveDuelEvent(duelEvent: DuelEvent)
}

/**
  * Actor-Instanz zur asynchronen Persistierung von Duel-Events
  */
class DuelEventPersister @Inject() (duelRepository: DuelRepository) extends Actor {
  override def receive: Receive = {
    case SaveDuelEvent(duelEvent) => {
      Logger.info("save: " + duelEvent.asString)
      duelRepository.saveDuelEvent(duelEvent.eventId, duelEvent)
    }
  }
}