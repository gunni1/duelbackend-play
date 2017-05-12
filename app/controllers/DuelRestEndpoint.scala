package controllers

import javax.inject._

import akka.actor.ActorSystem
import backend.avatar.persistence.{AvatarId, AvatarRepository}
import backend.simulation.DuelSimulator
import backend.simulation.DuelSimulator.InitiateDuelBetween
import backend.simulation.persistence.DuelRepository
import controllers.dto.InitiateDuelDto
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Aufrufe zu Duellen
  */
@Singleton
class DuelRestEndpoint @Inject() (actorSystem: ActorSystem, avatarRepository: AvatarRepository,
                                  duelRepository: DuelRepository) extends Controller {

  implicit val initiateDuelReads: Reads[InitiateDuelDto] = (
    (JsPath \ "leftAvatarId").read[String] and (JsPath \ "rightAvatarId").read[String] )(InitiateDuelDto.apply _)

  def initiateDuel = Action(BodyParsers.parse.json) { implicit request =>
    val parseResult = request.body.validate[InitiateDuelDto]
    parseResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      result => {
        val leftAvatar = avatarRepository.getAvatar(AvatarId(result.leftAvatarId))
        val rightAvatar = avatarRepository.getAvatar(AvatarId(result.rightAvatarId))
        if(leftAvatar.isDefined && rightAvatar.isDefined)
        {
            val duelId = duelRepository.nextDuelId
            val duelSimulator = actorSystem.actorOf(DuelSimulator.props, duelId.asString)


            duelSimulator  ! InitiateDuelBetween(leftAvatar.get, rightAvatar.get, duelId)
            //Asynchron duell starten
          Ok(Json.obj("status" -> "OK", "duelId" -> (duelId.asString) ))
        }
        else
        {
          NotFound(Json.obj("status" -> "NotFound", "message" -> "avatar not found" ))
        }
      }
    )
  }

}