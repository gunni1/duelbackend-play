package main

import backend.avatar.Avatar
import backend.simulation.DuelSimulator

/**
  * Created by gunni on 09.03.17.
  */
object Main {
  def main(args:Array[String]) = {
    val left = new Avatar("Hans")
    left.agility = 600
    left.strength = 600
    left.perception = 600
    left.dexterity = 600
    val right = new Avatar("Peter")
    right.agility = 500
    right.strength = 600
    right.perception = 600
    right.dexterity = 600

    val duelProtocol = new DuelSimulator().simulateDuelBetween(left,right)
    print(duelProtocol.asString)
    println("\nWinner: " + duelProtocol.getWinningAvatar.name)
  }
}
