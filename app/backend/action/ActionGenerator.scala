package backend.action

import scala.util.Random

/**
  * Generiert Aktionen in Abhängigkeit von gegebenen Wahrscheinlichkeiten.
  */
case class ActionGenerator(blockProb: BlockProbability, counterProb: CounterProbability) {
  def hitProb = 1 - blockProb.prob - counterProb.prob

  def blockInterval = (0.0,blockProb.prob)
  def counterInterval = (blockInterval._2, blockInterval._2 + counterProb.prob)
  def hitInterval = (counterInterval._2, 1.0)

  def printableProbs: String = "Hit: " + hitProb + " block: " + blockProb + " counter: " + counterProb

  def nextAction(random: Random): Action = {
    random.nextDouble() match {
      case x if inBlockInterval(x) => BlockedAttack()
      case x if inCounterInterval(x) => CounteredAttack()
      case x if inHitInterval(x) => Attack()
    }
  }

  def inBlockInterval(x: Double) = x >= blockInterval._1 && x < blockInterval._2
  def inCounterInterval(x: Double) = x>= counterInterval._1 && x < counterInterval._2
  def inHitInterval(x: Double) = x >= hitInterval._1 && x < hitInterval._2
}

