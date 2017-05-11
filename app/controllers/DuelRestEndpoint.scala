package controllers

import javax.inject._

import akka.actor.ActorSystem
import backend.avatar._
import backend.avatar.persistence.{AvatarId, AvatarRepository, NonePersistentAvatarRepository}
import backend.simulation.DuelSimulator
import controllers.dto.InitiateDuelDto
import play.api.Play
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Aufrufe zu Duellen
  */
@Singleton
class DuelRestEndpoint @Inject() (actorSystem: ActorSystem) extends Controller {
  val avatarRepository: AvatarRepository = NonePersistentAvatarRepository

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
            val duelName = leftAvatar.get.name + " vs " + rightAvatar.get.name
            val duelSimulator = actorSystem.actorOf(DuelSimulator.props, duelName)
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