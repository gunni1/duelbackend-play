package backend.simulation

import scala.collection.mutable.ListBuffer

/**
  * Listet Duellverlauf mit Wartezeiten und ausgeführten Aktionen auf.
  */
class DuelProtocolBuilder{
  private var log: ListBuffer[String]= new ListBuffer[String]

  /**
    * Aktion wird von Spieler ausgeführt. Hat auch Einfluss auf den jeweils anderen Character.
    */
  def logAction(executionResult: ExecutionResult) ={
    log += executionResult.asString
  }

  /**
    * Protokolliert ein Warte-Ereignis von einer bestimmten Dauer.
    */
  def logWait(units: Int) ={
    log += "Waited " + units + " units."
  }

  def finish(winningAvatar: FightingAvatar) = new DuelProtocol(log, winningAvatar)
}

class DuelProtocol(log: ListBuffer[String], winningAvatar: FightingAvatar){
  def asString: String = log.toList.filter{_.nonEmpty}.mkString("\n")

  def duelLog: List[String] = log.toList

  def getWinningAvatar = winningAvatar
}
