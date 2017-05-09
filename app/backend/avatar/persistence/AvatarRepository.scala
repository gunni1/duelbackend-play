package backend.avatar.persistence

import backend.avatar.{Attribute, Avatar}

/**
  * Ein Repository zum Erzeugen, Laden und ändern von Avataren.
  */
trait AvatarRepository  {
  /**
    * Liefert eine Liste aller Avatare
    */
  def listAvatars: List[AvatarModel]

  /**
    * Erzeugt einen neuen Avatar mit einem bestimmten Namen.
    */
  def createAvatar(name: String): AvatarId

  /**
    * Liefert einen Avatar anhand seiner AvatarId.
    */
  def getAvatar(avatarId: AvatarId): Option[Avatar]

  /**
    * Ändert einen Attributwert eines Avatars.
    */
  def updateAttribute(avatarId: AvatarId, attributeToUpdate: Attribute)
}
