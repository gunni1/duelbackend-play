package main

import backend.avatar.Avatar
import backend.avatar.persistence.AvatarId
import backend.duel.persistence.NonePersistentDuelRepository
import backend.simulation.{DuelSimulator, FightingAvatar}

import scala.util.Random

/**
  * Created by gunni on 09.03.17.
  */
object Main {
  def main(args:Array[String]) = {
    val left = new Avatar("Hans", AvatarId("1"))
    left.agility = 600
    left.strength = 600
    left.perception = 600
    left.dexterity = 600
    val right = new Avatar("Peter", AvatarId("2"))
    right.agility = 500
    right.strength = 600
    right.perception = 600
    right.dexterity = 600

    val duelRepository = new NonePersistentDuelRepository

    val fightsRandom = new Random()
    val fightingLeft = FightingAvatar(left, right,fightsRandom)
    val fightingRight = FightingAvatar(right, left,fightsRandom)

    val duelProtocol = new DuelSimulator(duelRepository).simulateDuelBetween(fightingLeft,fightingRight)
    print(duelProtocol.asString)
    println("\nWinner: " + duelProtocol.getWinningAvatar.name)
  }
}
