package controllers.dto

/**
  * Case Klasse zur Json-Konvertierung eine Attribut-Aktualisierung.
  */
case class UpdateAttributeDto(avatarId: String, attributeName: String, newValue: Int)