package backend.avatar.persistence

import java.text.ChoiceFormat

import backend.avatar._
import play.api.libs.openid.Errors.AUTH_CANCEL

import scala.collection._
import scala.collection.concurrent.TrieMap
/**
  * Eine Implementierung des AvatarRepository be ider die Daten im Speicher
  * gehalten werden.
  */
class NonePersistentAvatarRepository extends AvatarRepository{
  private val idGenerator = new AvatarIdGenerator
  private val avatars: concurrent.TrieMap[AvatarId, Avatar] = new TrieMap[AvatarId, Avatar]()

  /**
    * Liefert eine Liste aller Avatare
    */
  override def listAvatars: List[Avatar] =
    avatars.values.toList

  /**
    * Erzeugt einen neuen Avatar mit einem bestimmten Namen.
    */
  override def createAvatar(name: String): AvatarId = {
    val nextAvatarId = idGenerator.getId
    val avatar = new Avatar(name, nextAvatarId)
    avatars.put(nextAvatarId, avatar)
    nextAvatarId
  }

  /**
    * Liefert einen Avatar anhand seiner AvatarId.
    */
  override def getAvatar(avatarId: AvatarId): Option[Avatar] =  avatars.get(avatarId)

  /**
    * Ändert einen Attributwert eines Avatars.
    */
  override def updateAttribute(avatarId: AvatarId, attributeToUpdate: Attribute): Unit = {
    //Annahme: Avatar ist bereits geprüft und vorhanden
    val avatarToUpdate = getAvatar(avatarId).get
    attributeToUpdate match {
      case Strength(v) => avatarToUpdate.strength = v
      case Agility(v) => avatarToUpdate.agility = v
      case Endurance(v) => avatarToUpdate.endurance = v
      case Dexterity(v) => avatarToUpdate.dexterity = v
      case Perception(v) => avatarToUpdate.perception = v
    }
  }


  //Einige Avatare sind bereis vorhanden:
  createAvatar("Hans")
  createAvatar("Jochen")
  createAvatar("Peter")
}
