package controllers

import java.time.format.DateTimeFormatter
import javax.inject._

import akka.actor.{ActorRef, ActorSystem, Props}
import backend.avatar.persistence.{AvatarId, AvatarRepository}
import backend.duel._
import backend.duel.persistence.{DuelEventId, DuelEventRepository, DuelId}
import controllers.dto.{DuelRequestTimedOut, DuelStarted, InitiateDuelDto}
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Aufrufe zu Duellen
  */
@Singleton
class DuelRestEndpoint @Inject()(actorSystem: ActorSystem, avatarRepository: AvatarRepository,
                                 duelRepository: DuelEventRepository) extends Controller {

  val duelManager = new DuelManager(duelRepository, actorSystem)
  val duelReceptionist = new DuelReceptionist(duelRepository, duelManager)
  val execTimeService = new ActionExecutionTimeService

  val TIME_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME

  implicit val initiateDuelReads: Reads[InitiateDuelDto] = (
    (JsPath \ "leftAvatarId").read[String] and (JsPath \ "rightAvatarId").read[String]) (InitiateDuelDto.apply _)

  implicit val duelIdWrites: Writes[DuelId] = Json.writes[DuelId]
  implicit val duelStartedWrites: Writes[DuelStarted] = Json.writes[DuelStarted]
  implicit val duelEventIdWrites: Writes[DuelEventId] = Json.writes[DuelEventId]

  def getNextActionExecution(duelId: String) = Action { implicit request =>
    execTimeService.getNextExecution(DuelId(duelId)).map {
      execTime => Ok(Json.obj("status" -> "OK", "nextAction" -> execTime.format(TIME_FORMAT)))
    }.getOrElse(NotFound)
  }

  def getDuelEvent(duelId: String, eventId: String) = Action { implicit request =>
    duelRepository.loadDuelEvent(DuelEventId(DuelId(duelId), eventId)).map {
      duelEvent => duelEvent match {
        case ActionEvent(duelEventId, executionResult) => Ok
      }
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
          //TODO: Aus dem Response-Event ein Duell-Event machen und persistieren
          duelReceptionist.requestDuel(leftAvatar.get, rightAvatar.get) match {
            case response:DuelStarted => Ok(Json.toJson(response))
            case DuelRequestTimedOut() => Ok(Json.obj("status" -> "OK", "message" -> "duel not accepted"))
          }
        }
        else {
          NotFound(Json.obj("status" -> "NotFound", "message" -> "avatar not found"))
        }
      }
    )
  }

}