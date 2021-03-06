package controllers

import backend.avatar._
import backend.avatar.persistence.{AvatarId, AvatarRepository, NonePersistentAvatarRepository}
import controllers.dto.{AvatarDto, CreateAvatarDto, UpdateAttributeDto}
import javax.inject._

import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Anfragen zu Avataren.
  */
class AvatarController @Inject()(avatarRepository: AvatarRepository) extends Controller{


  implicit val avatarIdWrites: Writes[AvatarId] =
    (JsPath \ "id").write[String].contramap { (avatarId: AvatarId) => avatarId.id }

  implicit val avatarDtoWrites: Writes[AvatarDto] = (
    (JsPath \ "name").write[String] and (JsPath \ "avatarId").write[String] and
      (JsPath \ "str").write[Int] and (JsPath \ "agi").write[Int] and
      (JsPath \ "end").write[Int] and (JsPath \ "dex").write[Int] and
      (JsPath \ "perc").write[Int] )(unlift(AvatarDto.unapply))

  implicit val createAvatarReads: Reads[CreateAvatarDto] = Json.reads[CreateAvatarDto]

  implicit val updateAttributeReads: Reads[UpdateAttributeDto] = (
    (JsPath \ "avatarId").read[String] and (JsPath \ "attributeName").read[String] and
      (JsPath \ "newValue").read[Int])(UpdateAttributeDto.apply _)

  /**
    * Liefert eine Liste mit allen Avataren eines Users
    */
  def getAvatars(userId: String) = Action { implicit request =>
    var avatarDtos = avatarRepository.listAvatars(userId).map(avatar => AvatarDto(avatar))
    Ok(Json.toJson(avatarDtos))
  }

  /**
    * Liefert einen Avatar zu einer bestimmten AvatarId.
    */
  def getAvatar(avatarId: String) = Action { implicit request =>
    val maybeAvatar = avatarRepository.getAvatar(AvatarId(avatarId))

    maybeAvatar.map { avatar => Ok(Json.toJson(AvatarDto(avatar))) }.getOrElse(NotFound)
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
        val avatarId = avatarRepository.createAvatar(avatar.name, avatar.userId)
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
