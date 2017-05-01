package backend.persistence

import java.util.concurrent.atomic.AtomicLong

/**
  * Dient der Generierung einer eindeutigen Id neu erzeugter Avatare
  */
class AvatarIdGenerator {
  private val idCounter = new AtomicLong()
  def getId: AvatarId = {
    new AvatarId(idCounter.incrementAndGet.toString)
  }
}
