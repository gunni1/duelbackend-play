package backend.avatar

/**
  * Created by gunni on 01.05.2017.
  */
trait Attribute
case class Strength(value: Int) extends Attribute
case class Agility(value: Int) extends Attribute
case class Endurance(value: Int) extends Attribute
case class Dexterity(value: Int) extends Attribute
case class Perception(value: Int) extends Attribute
