package backend.persistence

import backend.avatar.Avatar

/**
  * Repräsentiert einen Avatar aus Sicht einer externen Komponente.
  * Der Avatar besitzt zusätzlich eine endeutige ID zur externen Referenzierung.
  */
case class AvatarModel(avatarId: String, name: String,
                       strength: Int, agility: Int, endurance: Int, dexterity: Int, perception: Int){

  def this(avatarId: AvatarId, avatar: Avatar) { this(avatarId.id, avatar.name, avatar.strength, avatar.agility,
    avatar.endurance, avatar.dexterity, avatar.perception)
  }

  def apply(avatarId: AvatarId, avatar: Avatar): AvatarModel = new AvatarModel(avatarId.id, avatar.name,
    avatar.strength, avatar.agility, avatar.endurance, avatar.dexterity, avatar.perception)
}



