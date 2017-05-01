package backend.avatar

/**
  * Ein Spielcharakter.
  */
class Avatar(val name: String) {
  /**
    * Beeinflusst Schlagstärke
    */
  var strength: Int = 1

  /**
    * Beweglichkeit bestimmt die Dauer bis zur nächsten Aktion. Zusätzich beeinflusst Beweglichkeit
    * die Wahrscheinlichkeit gegnerische Angriffe zu kontern und vom gegner ausgekontert zu werden.
    */
  var agility: Int = 1

  /**
    * nicht direkt steigerbar?
    */
  var endurance: Int = 1

  /**
    * Ein hoher Wert senkt die Wahrscheinlichkeit dass Angriffe durch den Gegner geblockt oder gekontert werden.
    */
  var dexterity: Int = 1

  /**
    * Erhöht die Wahrscheinlichkeit einen gegnerischen Angriff rechtzeitig kommen zu sehen und
    * zu Blocken oder zu kontern.
    */
  var perception: Int = 1
}
