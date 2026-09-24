---
description: Eine Methode mit Characterization Tests Schicht für Schicht unter Test bringen, bis jede Verzweigung und jeder Grenzwert festgehalten ist
agent: test-autor
---
Bring eine Methode mit Characterization Tests unter Test. Methode: $ARGUMENTS

Skill characterization-test, hier eingefügt:
@.opencode/skills/characterization-test/SKILL.md

Produktivcode:
!`find src/main -name '*.java' | sort`

Vorhandene Tests:
!`find src/test -name '*.java' | sort`

Stand der Abdeckung, leer, wenn noch kein Bericht da ist:
!`scripts/unabgedeckt.sh 2>/dev/null | head -40`

Vorgehen:
1. Steht oben keine Methode als `Klasse#methode`: frag, welche Methode es sein soll, nenne die Kandidaten aus dem Produktivcode und warte. Nichts lesen, nichts schreiben, bevor die Methode feststeht.
2. Lies die Methode ganz und schreibe die Landkarte aus dem Skill als Text in deine Antwort, bevor du irgendeine Datei anlegst: Eingänge, Ausgänge, nummerierte Verzweigungen mit ihren Vergleichswerten.
3. Grabe nach den Schichten im Skill, ein Test je Schritt: einen Test schreiben, `mvn -q verify`, dann `scripts/unabgedeckt.sh <Klasse>`, dann erst der nächste Test. Kein Patch mit mehreren Tests auf einmal, auch nicht am Anfang. Reihenfolge: geradeaus, dann Verzweigung für Verzweigung, jede Kante als Paar (beide Seiten), Schleifen mit null, einem und mehreren Elementen.
4. Fertig nach der Liste im Skill. Am Ende: Anzahl der Tests, die Zeile „Zusammenfassung“ aus `scripts/unabgedeckt.sh <Klasse>`, die Landkarte mit dem Testnamen je Verzweigungsseite und je Kante, die Beobachtungen.

Regeln:
- Kein Produktivcode wird angefasst. Die Tests passen sich dem Code an, nie umgekehrt.
- Keine Tests löschen, kein `@Ignore`.
- Neue Tests in die vorhandene Testklasse des Gegenstands, sonst eine neue Klasse, nach dem Gegenstand benannt.
