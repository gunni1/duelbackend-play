package backend.simulation

/**
  * Zeitsteuerung für das Duell
  */
class DuelTimer(left: FightingAvatar, right: FightingAvatar) {
  var actualTimeLeft = left.actionTime
  var actualTimeRight = right.actionTime

  /**
    * Liefert die Wartezeit bis zur nächsten Aktion und den Avatar,
    * der als nächstes eine Aktion ausführen darf.
    */
  def next: TimerResult = {
    if(actualTimeLeft < actualTimeRight) {
      val waited = actualTimeLeft
      actualTimeRight = actualTimeRight - actualTimeLeft
      actualTimeLeft = left.actionTime
      return TimerResult(waited, left, right)
    }
    else {
      val waited = actualTimeRight
      actualTimeLeft = actualTimeLeft - actualTimeRight
      actualTimeRight = right.actionTime
      return TimerResult(waited, right, left)
    }
  }
}

case class TimerResult(nextActionIn: Int, executing: FightingAvatar, executedOn: FightingAvatar)
