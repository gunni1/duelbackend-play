package controllers

import backend.avatar._
import backend.persistence.{AvatarId, AvatarRepository, MemoryAvatarRepository}
import controllers.dto.{CreateAvatarDto, InitiateDuelDto, UpdateAttributeDto}
import play.api.Play
import play.api.Play
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Aufrufe zu Duellen
  */
class DuelRestEndpoint  extends Controller {
  val avatarRepository: AvatarRepository = MemoryAvatarRepository

  implicit val updateAttributeReads: Reads[InitiateDuelDto] = (
    (JsPath \ "leftAvatarId").read[String] and (JsPath \ "rightAvatarId").read[String] )(InitiateDuelDto.apply _)

  def initiateDuel = Action(BodyParsers.parse.json) { implicit request =>
    val parseResult = request.body.validate[InitiateDuelDto]
    parseResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      result => {
        val leftAvatar = avatarRepository.getAvatar(AvatarId(result.leftAvatarId))
        val rightAvatar = avatarRepository.getAvatar(AvatarId(result.leftAvatarId))
        if(leftAvatar.isDefined && rightAvatar.isDefined)
        {
            //Asynchron duell starten
          Ok(Json.obj("status" -> "OK", "message" -> ("Avatar ") ))
        }
        else
        {
          NotFound(Json.obj("status" -> "NotFound", "message" -> "avatar not found" ))
        }
      }
    )
  }

}
