package backend

import backend.avatar.persistence.{AvatarRepository, NonePersistentAvatarRepository}
import backend.duel.persistence.{DuelRepository, NonePersistentDuelRepository}
import com.google.inject.AbstractModule

/**
  * Dependency Injection Definition
  */
class BackendModule extends AbstractModule{

  def configure() = {
    bind(classOf[AvatarRepository]).to(classOf[NonePersistentAvatarRepository]).asEagerSingleton
    bind(classOf[DuelRepository]).to(classOf[NonePersistentDuelRepository]).asEagerSingleton
  }
}
