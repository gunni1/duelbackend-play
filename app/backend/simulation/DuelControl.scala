package backend.simulation

/**
  * Duellsteuerung. Gibt Auskunft ob das Duell noch im Gang ist und verwaltet das Timing von Aktionen
  */
case class DuelControl(left: FightingAvatar, right: FightingAvatar) {
  def doNextActionUntilFinished: DuelProtocol = {
    val protocol = new DuelProtocolBuilder
    val duelTimer = new DuelTimer(left, right)

    while(isNotFinished)
    {
      duelTimer.next match {
        case TimerResult(waited, next) if next.equals(left) => {
          protocol.logWait(waited)
          protocol.logAction(left.execute(left.nextAction).on(right))
        }
        case TimerResult(waited, next) if next.equals(right) => {
          protocol.logWait(waited)
          protocol.logAction(right.execute(right.nextAction).on(left))
        }
      }
    }

    protocol.finish(getWinner)
  }

  private def isNotFinished: Boolean = left.actualEnergy > 0 && right.actualEnergy > 0

  private def getWinner = {
    if(left.actualEnergy <= 0) right
    else left
  }
}
