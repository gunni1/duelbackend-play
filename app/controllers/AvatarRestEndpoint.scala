package controllers

import backend.avatar._
import controllers.model.CreateAvatarModel
import play.api.Play
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Anfragen zu Avataren.
  */
class AvatarRestEndpoint extends Controller{
  val avatarRepository: AvatarRepository = new MemoryAvatarRepository

  implicit val createAvatarWrite: Writes[AvatarModel] = (
    (JsPath \ "avatarId").write[String] and (JsPath \ "name").write[String] and
      (JsPath \ "strength").write[Int] and (JsPath \ "agility").write[Int] and
      (JsPath \ "endurance").write[Int] and (JsPath \ "dexterity").write[Int] and
      (JsPath \ "perception").write[Int] )(unlift(AvatarModel.unapply))

  implicit val createAvatarReads: Reads[CreateAvatarModel] =
    (JsPath \ "name").read[String].map(CreateAvatarModel(_))

  def getAvatars = Action { implicit request =>

    val avatarsJson = Json.toJson(avatarRepository.listAvatars)
    Ok(avatarsJson)
  }

  def getAvatar(avatarId: String) = Action { implicit request =>
    val maybeAvatar = avatarRepository.getAvatar(AvatarId(avatarId))

    maybeAvatar.map { avatar => Ok(Json.toJson(new AvatarModel(AvatarId(avatarId), avatar))) }.getOrElse(NotFound)
  }

  def createAvatar = Action(BodyParsers.parse.json) { request =>
    val parseResult = request.body.validate[CreateAvatarModel]
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
}
