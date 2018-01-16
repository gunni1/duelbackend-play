package backend

import backend.avatar.persistence.{AvatarRepository, MongoDBAvatarRepository, NonePersistentAvatarRepository}
import backend.duel.persistence.{DuelEventRepository, NonePersistentDuelEventRepository}
import com.google.inject.AbstractModule

/**
  * Dependency Injection Definition
  */
class BackendModule extends AbstractModule{

  def configure() = {
    bind(classOf[AvatarRepository]).to(classOf[NonePersistentAvatarRepository]).asEagerSingleton
    //bind(classOf[AvatarRepository]).to(classOf[MongoDBAvatarRepository]).asEagerSingleton
    bind(classOf[DuelEventRepository]).to(classOf[NonePersistentDuelEventRepository]).asEagerSingleton
  }
}
