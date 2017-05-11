package controllers

import backend.avatar._
import backend.avatar.persistence.{AvatarId, AvatarModel, AvatarRepository, NonePersistentAvatarRepository}
import controllers.dto.{CreateAvatarDto, UpdateAttributeDto}
import play.api.Play
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Anfragen zu Avataren.
  */
class AvatarRestEndpoint extends Controller{
  val avatarRepository: AvatarRepository = NonePersistentAvatarRepository

  implicit val createAvatarWrite: Writes[AvatarModel] = (
    (JsPath \ "avatarId").write[String] and (JsPath \ "name").write[String] and
      (JsPath \ "strength").write[Int] and (JsPath \ "agility").write[Int] and
      (JsPath \ "endurance").write[Int] and (JsPath \ "dexterity").write[Int] and
      (JsPath \ "perception").write[Int] )(unlift(AvatarModel.unapply))

  implicit val createAvatarReads: Reads[CreateAvatarDto] =
    (JsPath \ "name").read[String].map(CreateAvatarDto(_))

  implicit val updateAttributeReads: Reads[UpdateAttributeDto] = (
    (JsPath \ "avatarId").read[String] and (JsPath \ "attributeName").read[String] and
      (JsPath \ "newValue").read[Int])(UpdateAttributeDto.apply _)

  /**
    * Liefert eine Liste mit allen Avataren und ihren Attributwerten (Testzwecke)
    */
  def getAvatars = Action { implicit request =>
    val avatarsJson = Json.toJson(avatarRepository.listAvatars)
    Ok(avatarsJson)
  }

  /**
    * Liefert einen Avatar zu einer bestimmten AvatarId.
    */
  def getAvatar(avatarId: String) = Action { implicit request =>
    val maybeAvatar = avatarRepository.getAvatar(AvatarId(avatarId))

    maybeAvatar.map { avatar => Ok(Json.toJson(new AvatarModel(AvatarId(avatarId), avatar))) }.getOrElse(NotFound)
  }

  /**
    * Empfängt ein Json mit einem Namen und erzeugt unter diesem Namen einen neuen Avatar.
    */
  def createAvatar = Action(BodyParsers.parse.json) { request =>
    val parseResult = request.body.validate[CreateAvatarDto]
    parseResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      avatar => {
        val avatarId = avatarRepository.createAvatar(avatar.name)
        Ok(Json.obj("status" -> "OK", "message" -> ("Avatar '"+avatar.name + "' saved with id: " + avatarId) ))
      }
    )
  }

  def updateAttribute= Action(BodyParsers.parse.json) {request =>
    val parseResult = request.body.validate[UpdateAttributeDto]
    parseResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      updateAttribute => {
        val avatarId = AvatarId(updateAttribute.avatarId)
        val attributeToUpdate = parseAttribute(updateAttribute.attributeName, updateAttribute.newValue)

        if(avatarRepository.getAvatar(avatarId).isDefined && attributeToUpdate.isDefined){
          avatarRepository.updateAttribute(avatarId, attributeToUpdate.get)
        }
        Ok
      }
    )
  }

  private def parseAttribute(attributeString: String, value: Int): Option[Attribute] = attributeString match {
    case "strength" => Some(Strength(value))
    case "agility" => Some(Agility(value))
    case "endurance" => Some(Endurance(value))
    case "dexterity" => Some(Dexterity(value))
    case "perception" => Some(Perception(value))
    case _ => None
  }
}
