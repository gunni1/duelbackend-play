package controllers

import backend.persistence.{AvatarRepository, MemoryAvatarRepository}
import play.api.mvc.{Action, BodyParsers}
import play.mvc.Controller

/**
  * Bedient Rest-Aufrufe zu Duellen
  */
class DuelRestEndpoint  extends Controller {
  val avatarRepository: AvatarRepository = MemoryAvatarRepository

  def initiateDuel = Action(BodyParsers.parse.json) { implicit request =>

  }
}
