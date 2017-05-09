package backend.simulation.persistence

import java.util.concurrent.atomic.AtomicLong

/**
  * Created by gunni on 09.05.17.
  */
class DuelIdGenerator {
  private val idCounter = new AtomicLong()
  def getId: DuelId = {
    new DuelId(idCounter.incrementAndGet.toString)
  }
}
