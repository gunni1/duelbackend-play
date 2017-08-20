package backend.avatar.persistence

import backend.avatar.{Attribute, Avatar}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{Completed, MongoClient, MongoDatabase, Observer}
import org.mongodb.scala.ScalaObservable

import scala.concurrent.Await

class MongoDBAvatarRepository extends AvatarRepository {

  val mongoClient = MongoClient("mongodb://localhost")
  val database: MongoDatabase = mongoClient.getDatabase("mongodb");
  val AvatarCollectionName = "avatars"


  /**
    * Liefert eine Liste aller Avatare
    */
  override def listAvatars(userId: String): List[Avatar] = {
    return null
  }

  /**
    * Erzeugt einen neuen Avatar mit einem bestimmten Namen.
    */
  override def createAvatar(name: String, userId: String): AvatarId = {
    val id = new ObjectId().toHexString
    val document = Document("_id" -> id, "name" -> name, "userId" -> userId, "strength" -> 1, "agility" -> 1, "endurance" -> 1,
      "dexterity" -> 1, "perception" -> 1)
    val avatarCollection = database.getCollection(AvatarCollectionName)
    avatarCollection.insertOne(document).subscribe((_: Completed) => println("inserted"))
    return AvatarId(id)
  }

  /**
    * Liefert einen Avatar anhand seiner AvatarId.
    */
  override def getAvatar(avatarId: AvatarId): Option[Avatar] = None

  /**
    * Ändert einen Attributwert eines Avatars.
    */
  override def updateAttribute(avatarId: AvatarId, attributeToUpdate: Attribute): Unit = print("")
}
