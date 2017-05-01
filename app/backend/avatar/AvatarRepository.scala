package backend.avatar

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
}
