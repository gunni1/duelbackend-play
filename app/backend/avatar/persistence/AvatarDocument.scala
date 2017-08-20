package backend.avatar.persistence

import org.mongodb.scala.bson.ObjectId

/**
  * Dokumentenklasse zur Persistierung von Avataren in einer MongoDB.
  */
case class AvatarDocument(_id: ObjectId, userId: String, name: String, strength: Int, agility: Int, endurance: Int,
                          dexterity: Int, perception: Int)

object AvatarDocument {
  def apply(userId: String, name: String, strength: Int, agility: Int, endurance: Int, dexterity: Int, perception: Int):
  AvatarDocument = AvatarDocument(new ObjectId(), userId, name, strength, agility, endurance, dexterity, perception)
}
