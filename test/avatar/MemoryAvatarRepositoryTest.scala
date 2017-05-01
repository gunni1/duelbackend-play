package avatar

import backend.persistence.{AvatarId, MemoryAvatarRepository}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by gunni on 21.03.17.
  */
class MemoryAvatarRepositoryTest extends FlatSpec with Matchers{
  "A AvatarRepository" should "create two avatars with different ids" in {
    val avatarRepository = new MemoryAvatarRepository()

    val idOfHans = avatarRepository.createAvatar("Hans")
    val idOfFrank = avatarRepository.createAvatar("Frank")
    idOfHans should equal (AvatarId("1"))
    idOfFrank should equal (AvatarId("2"))
  }

  it should "supply a created avatar by given id" in {
    val avatarRepository = new MemoryAvatarRepository()
    val idOfHans = avatarRepository.createAvatar("Hans")
    val maybeHans = avatarRepository.getAvatar(idOfHans)

    maybeHans.isDefined should be (true)
    maybeHans.get.name should be ("Hans")
  }

  it should "supply a empty option when asked for a unset id" in {
    val maybeAvatar = new MemoryAvatarRepository().getAvatar(AvatarId("1"))
    maybeAvatar.isDefined should be (false)
  }
}

