package controllers.dto

import java.time.ZonedDateTime

/**
  * Created by gunni on 09.07.2017.
  */
case class UserCommandDto(commandType: String, avatarId: String, duelId: String, issuedAt: ZonedDateTime)
