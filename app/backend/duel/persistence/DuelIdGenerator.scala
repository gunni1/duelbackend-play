package backend.duel.persistence

import java.util.concurrent.atomic.AtomicLong

/**
  * Created by gunni on 09.05.17.
  */
object DuelIdGenerator {
  private val idCounter = new AtomicLong()

  /**
    * Liefert die nächste Duell-ID
    */
  def nextId: DuelId = {
    new DuelId(idCounter.incrementAndGet.toString)
  }
}
