package backend.simulation

import backend.action.{Action, ActionGenerator, BlockProbability, CounterProbability}
import backend.avatar.Avatar

import scala.util.Random

/**
  * Repräsentiert einen Avatar während eines Duells. Er besitzt einen aktuellen Energie-Wert
  * und Attribute werden zu ActionTime umgerechnent.
  *
  * Da Wahrscheinlichkeiten für Aktionen immer auch vom Gegner abhängen wird dieser mit benötigt.
  */
case class FightingAvatar(ownAvatar: Avatar, opponent: Avatar, random: Random){
  private var energy = 100

  val actionGenerator = ActionGenerator(BlockProbability(ownAvatar, opponent),
    CounterProbability(ownAvatar, opponent))

  def name = ownAvatar.name

  def actualEnergy = energy

  def nextAction = actionGenerator.nextAction(random)

  /**
    * Die ActionTime ist die Wartezeit bis zur nächsten Aktion in Millisekunden
    * Je mehr agility, desto geringer die Actiontime.
    * >> Vorerst einfache Formel: 2000-agi
    */
  def actionTime = 2000 - ownAvatar.agility

  def receiveDamage(damage: Int) = energy -= damage

  /**
    * Angriffstsärke zwischen 1 und 16 je 1024 Stärke.
    * Wenn kleiner 64 nicht 0 sondern 1
    */
  def strikeStrength: Int = ownAvatar.strength match {
    case strength if strength < 1024/16 => 1
    case _ => ((ownAvatar.strength / 1024d) * 16).toInt
  }

  /**
    * Angriffsgenauigkeit zwischen 1 und strikeStrength/2 je 512 Dex ????
    * Momentan erstmal immer nur 1
    */
  def strikeAccuracy = 1
  /**
    * Bereitet eine Aktionsausführung vor. Verpackt den Ausführenden und eine zufällig gewürfelte
    * Aktion um sie auf einen anderen FightingAvatar auszuführen.
    */
  def execute(action: Action) = new ActionExecution(this, action, random)
}
