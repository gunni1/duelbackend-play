package backend.avatar

import backend.avatar.persistence.AvatarId

/**
  * Eine Entity die einen Spielcharakter repräsentiert.
  */
case class Avatar(name: String, avatarId: AvatarId, var strength: Int = 1, var agility: Int = 1, var endurance: Int = 1,
             var dexterity: Int = 1, var perception: Int = 1) {
  /**
    * strength:
    * Beeinflusst Schlagstärke
    *
    * agility:
    * Beweglichkeit bestimmt die Dauer bis zur nächsten Aktion. Zusätzich beeinflusst Beweglichkeit
    * die Wahrscheinlichkeit gegnerische Angriffe zu kontern und vom gegner ausgekontert zu werden.
    *
    * endurance:
    * noch unklar
    *
    * dexterity:
    * Ein hoher Wert senkt die Wahrscheinlichkeit dass Angriffe durch den Gegner geblockt oder gekontert werden.
    *
    * perception:
    * Erhöht die Wahrscheinlichkeit einen gegnerischen Angriff rechtzeitig zu erkennen und
    * zu blocken oder zu kontern.
    */

  def apply(name: String, avatarId: AvatarId, strength: Int, agility: Int, endurance: Int,
            dexterity: Int, perception: Int): Avatar = new Avatar(name, avatarId,
    strength, agility, endurance, dexterity,perception)

  def unapply(arg: Avatar): Option[(String, AvatarId, Int, Int, Int, Int, Int )] =
    Some(name, avatarId, strength, agility, endurance, dexterity, perception)
}


