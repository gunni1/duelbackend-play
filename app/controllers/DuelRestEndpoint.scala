package controllers

import javax.inject._

import akka.actor.{ActorRef, ActorSystem, Props}
import backend.avatar.persistence.{AvatarId, AvatarRepository}
import backend.duel.DuelReceptionist
import backend.duel.dto.{DuelRequestTimedOut, DuelStarted}
import backend.duel.persistence.{DuelId, DuelProtocolModel, DuelRepository}
import backend.simulation.DuelSimulator
import backend.simulation.DuelSimulator.InitiateDuelBetween
import controllers.dto.InitiateDuelDto
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

import scala.collection.mutable

/**
  * Bedient Rest-Aufrufe zu Duellen
  */
@Singleton
class DuelRestEndpoint @Inject()(actorSystem: ActorSystem, avatarRepository: AvatarRepository,
                                 duelRepository: DuelRepository) extends Controller {

  val duelReceptionist = new DuelReceptionist(duelRepository, actorSystem)

  implicit val duelProtocolWrites: Writes[DuelProtocolModel] = (
    (JsPath \ "duelId").write[String] and (JsPath \ "duelLog").write[Seq[String]] and
      (JsPath \ "winner").write[String]) (unlift(DuelProtocolModel.unapply))


  implicit val initiateDuelReads: Reads[InitiateDuelDto] = (
    (JsPath \ "leftAvatarId").read[String] and (JsPath \ "rightAvatarId").read[String]) (InitiateDuelDto.apply _)

  implicit val duelIdWrites:Writes[DuelId] = Json.writes[DuelId]
  implicit val duelStartedWrites:Writes[DuelStarted] = Json.writes[DuelStarted]


  def getDuelProtocol(duelId: String) = Action { implicit request =>
    val maybeDuelProtocol = duelRepository.getDuelProtocol(DuelId(duelId))
    maybeDuelProtocol.map { protocol =>
      Ok(Json.toJson(DuelProtocolModel(
        duelId, protocol.duelLog, protocol.getWinningAvatar.name)))
    }.getOrElse(NotFound)
  }

  def initiateDuel = Action(BodyParsers.parse.json) { implicit request =>
    val parseResult = request.body.validate[InitiateDuelDto]
    parseResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      result => {
        val leftAvatar = avatarRepository.getAvatar(AvatarId(result.leftAvatarId))
        val rightAvatar = avatarRepository.getAvatar(AvatarId(result.rightAvatarId))
        if (leftAvatar.isDefined && rightAvatar.isDefined) {
          duelReceptionist.requestDuel(leftAvatar.get, rightAvatar.get) match {
            case response:DuelStarted => Ok(Json.toJson(response))
            //case DuelStarted(duelId, reactionTimeMillis) => Ok(Json.toJson(new DuelStarted(duelId, reactionTimeMillis)))
            case DuelRequestTimedOut() => Ok(Json.obj("status" -> "OK", "message" -> "duel not accepted"))
          }


          //Ok(Json.obj("status" -> "OK", "duelId" -> (duelId.asString)))
        }
        else {
          NotFound(Json.obj("status" -> "NotFound", "message" -> "avatar not found"))
        }
      }
    )
  }

}