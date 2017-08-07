package controllers.dto

import backend.avatar.Avatar

case class AvatarDto(name: String, avatarId: String, strength: Int, agility: Int, endurance: Int,
dexterity: Int, perception: Int) {
  def this(avt: Avatar) =
    this(avt.name, avt.avatarId.id, avt.strength, avt.agility,avt.endurance,avt.dexterity, avt.perception)


}

object AvatarDto {
  def apply(avt: Avatar) = new AvatarDto(avt)
}


