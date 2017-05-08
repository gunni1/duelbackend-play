package controllers.dto

/**
  * DTO für den POST-Request zum starten eines Duells zwischen zwei Avataren.
  */
case class InitiateDuelDto (leftAvatarId: String, rightAvatarId: String)
