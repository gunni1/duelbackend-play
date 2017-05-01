package backend.simulation

import backend.avatar.Avatar

import scala.util.Random

/**
  * Created by gunni on 18.02.17.
  */
class DuelSimulator {
  def simulateDuelBetween(leftAvatar: Avatar, rightAvatar: Avatar): DuelProtocol = {
    val fightsRandom = new Random()
    val left = FightingAvatar(leftAvatar, rightAvatar,fightsRandom)
    val right = FightingAvatar(rightAvatar, leftAvatar,fightsRandom)

    val duelControl = new DuelControl(left,right)

     duelControl.doNextActionUntilFinished
  }
}

