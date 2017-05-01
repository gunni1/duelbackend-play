package backend.action

import backend.avatar.Avatar


/**
  * gegner: perc        eigen: dex
  */
case class BlockProbability(own: Avatar, other: Avatar){
  val baseBlockProb = 0.2
  val percToDexRatio = other.perception / own.dexterity

  def prob = baseBlockProb * percToDexRatio
}

/**
  * gegner: perc (0.5)    eigen:  dex (0.6)
  *         agi  (0.2)            agi (0.4)
  *         dex  (0.3)
  */
case class CounterProbability(own: Avatar, other: Avatar) {
  val baseCounterProb = 0.1
  val otherStatSummary =  (other.perception * 0.5) +
    (other.agility * 0.2) +
    (other.dexterity * 0.3)
  val ownStatSummary =    (own.dexterity * 0.6) +
    (own.agility * 0.4)
  val ownToOtherStatRatio = otherStatSummary / ownStatSummary
  def prob = baseCounterProb * ownToOtherStatRatio
}
