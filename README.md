# duelbackend-play
Backend für Duell-App mit Play2-Framework

Zweispieler-Duell-Spiel mit Kämpfen basierend auf Zufallsaktionen beeinflusst durch Attributwerte.

# Architekturüberblick

Es wird ein Duell als Abfolge von Aktionen der Avatare simuliert. Eine Aktion wird von einem Duellanten auf den anderen ausgeführt und wird anhand der Attributwerte zufällig bestimmt.
Der Duellverlauf wird durch eine Kette von Aktionen abgebldet wobei jeder Schritt als DuelEvent gespeichert ist. Bei jeder Aktion wird die Zeit bis zur nächsten berechnet und über das Actor-Framework eingereiht. Neben den Aktions-Aktoren werden weitere Nachrichten an Aktoren versendet um asynchron z.B. die Aktionen in der DB zu protokollieren oder für die UI zu hinterlegen, wann die nächste Aktion ausgeführt wird.
Aktionen können durch Benutzeraktionen beeinflusst werden. Konkret existiert eine Schnittstelle um für ein Duell eine Aktion für die jeweils nächste Aktion zu hinterlegen.

## DuelReceptionist
Der DuelReceptionist verwaltet die Duell-Forderung zwischen zwei Spielern. Läd alle nötigen Daten und startet das Duell mit dem DuellManager.

## DuelManager
Der DuelManager feuert das initiale Duell-Event und beginnt die Aktionskette. Er instanziiert den DuelSimulator (Aktor), der Aktions-Events feuert und selbst darauf reagiert um Aktionen auszuführen.

Darüber hinaus werden über den DuelManager Benutzeraktionen an den DuelSimulator übergeben. Diese Funktion muss nicht unbedingt im DuelManager liegen, der einfachheit halber ist es erstmal hier implementiert.

## DuelSimulator

Der DuelSimulator führt Aktionen in der Aktionskette aus. Er reagiert auf folgende Aktor-Nachrichten:
- InitiateDuelBetween: Beginnt Kette. Bestimmt welcher Avatar die erste Aktion ausführt und reiht das entsprechende Event ein.
- IssueUserCommand: Setzt für einen Avatar eine durch den Spieler getriggerte Benutzerinteraktion für die nächste Aktion. Es kann je Aktion immer nur eine Benutzeraktion geben.
- ExecuteNextAction: Würfelt die nächste Aktion und führt sie auf die Avatare der Spieler aus. Bestimmt die Zeit bis zur nächsten Aktion und wer der ausführende Avatar ist.
