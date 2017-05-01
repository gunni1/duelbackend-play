package backend.avatar

import scala.collection._
import scala.collection.concurrent.TrieMap
/**
  * Eine Implementierung des AvatarRepository be ider die Daten im Speicher
  * gehalten werden.
  */
class MemoryAvatarRepository extends AvatarRepository{
  private val idGenerator = new AvatarIdGenerator
  private val avatars: concurrent.TrieMap[AvatarId, Avatar] = new TrieMap[AvatarId, Avatar]()

  /**
    * Liefert eine Liste aller Avatare
    */
  override def listAvatars: List[AvatarModel] =
    avatars.map { case (avatarId, avatar) => new AvatarModel(avatarId, avatar)}.toList

  /**
    * Erzeugt einen neuen Avatar mit einem bestimmten Namen.
    */
  override def createAvatar(name: String): AvatarId = {
    val nextAvatarId = idGenerator.getId
    val avatar = new Avatar(name)
    avatars.put(nextAvatarId, avatar)
    nextAvatarId
  }

  /**
    * Liefert einen Avatar anhand seiner AvatarId.
    */
  override def getAvatar(avatarId: AvatarId): Option[Avatar] =  avatars.get(avatarId)
  createAvatar("Hans")
}
