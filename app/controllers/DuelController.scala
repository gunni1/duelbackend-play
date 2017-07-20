package controllers

import java.time.format.DateTimeFormatter
import javax.inject._

import akka.actor.{ActorRef, ActorSystem, Props}
import backend.avatar.persistence.{AvatarId, AvatarRepository}
import backend.duel._
import backend.duel.persistence.{DuelEventId, DuelEventRepository, DuelId}
import controllers.dto.DuelEventDtoFactory.toDto
import controllers.dto._
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
  * Bedient Rest-Aufrufe zu Duellen
  */
@Singleton
class DuelController @Inject()(actorSystem: ActorSystem, avatarRepository: AvatarRepository,
                               duelRepository: DuelEventRepository) extends Controller {

  val execTimeService = new ActionExecutionTimeService
  val duelManager = new DuelManager(duelRepository, actorSystem, execTimeService)
  val duelReceptionist = new DuelReceptionist(duelRepository, duelManager)

  val TIME_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME

  implicit val initiateDuelReads: Reads[InitiateDuelDto] = (
    (JsPath \ "leftAvatarId").read[String] and (JsPath \ "rightAvatarId").read[String]) (InitiateDuelDto.apply _)

  implicit  val avatarIdFormat: Format[AvatarId] = Json.format[AvatarId]
  implicit val duelIdFormat: Format[DuelId] = Json.format[DuelId]
  implicit val duelStartedWrites: Writes[DuelStarted] = Json.writes[DuelStarted]
  implicit val duelEventIdWrites: Writes[DuelEventId] = Json.writes[DuelEventId]
  implicit val damageReceivedDtoWrites: Writes[DamageReceivedDto] = Json.writes[DamageReceivedDto]
  implicit val executionResultDtoWrites: Writes[ExecutionResultDto] = Json.writes[ExecutionResultDto]
  implicit val duelEventDtoWrites: Writes[DuelEventDto] = Json.writes[DuelEventDto]
  implicit val userCommandDtoReads: Reads[UserCommandDto] = Json.reads[UserCommandDto]

  def getNextActionExecution(duelId: String) = Action { implicit request =>
    execTimeService.getNextExecution(DuelId(duelId)).map {
      execTime => Ok(Json.obj("status" -> "OK", "nextAction" -> execTime.format(TIME_FORMAT)))
    }.getOrElse(NotFound)
  }

  def getDuelEvent(duelId: String, eventId: String) = Action { implicit request =>
    duelRepository.loadDuelEvent(DuelEventId(DuelId(duelId), eventId)).map {
      duelEvent => duelEvent match {
        case ActionEvent(duelEventId,executionResult) => Ok(Json.toJson(
          DuelEventDto(duelEventId, "ActionEvent", DuelEventDtoFactory.toDto(executionResult))))
        case AvatarLose(duelEventId,executionResult) => Ok(Json.toJson(
          DuelEventDto(duelEventId, "AvatarLose", DuelEventDtoFactory.toDto(executionResult))))
        case _ => NotFound(Json.obj("status" -> "500", "message" -> "unknown DuelEvent type"))
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

  def issueUserCommand = Action(BodyParsers.parse.json) { implicit request =>
    val response = request.body.validate[UserCommandDto].fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      result => {
        determUserCommand(result).map {
          userCommand => duelManager.issueUserCommand(userCommand).map {
            someError => NotFound(Json.obj("status" -> "NotFound", "message" -> someError))
          }.getOrElse(Ok)
        }.getOrElse(NotFound(Json.obj("status" -> "NotFound", "message" -> "Unknown command type")))
      }
    )
    response
  }

  private def determUserCommand(dto: UserCommandDto): Option[UserCommand] = dto.commandType match {
    case "Resign" => Some(Resign(AvatarId(dto.avatarId), DuelId(dto.duelId), dto.issuedAt))
    case _ => None
  }

}