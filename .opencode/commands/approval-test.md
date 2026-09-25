---
description: Golden Master über eine Methode mit ApprovalTests, das Raster aus den Literalen der Methode, jede Kante mit Nachbarn, genehmigt als Datei
agent: test-autor
---
Leg ein Golden Master über eine Methode an. Methode: $ARGUMENTS

Skill approval-test, hier eingefügt:
@.opencode/skills/approval-test/SKILL.md

Produktivcode:
!`find src/main -name '*.java' | sort`

Vorhandene Tests und genehmigte Dateien:
!`find src/test -type f | sort`

Vorgehen:
1. Steht oben keine Methode als `Klasse#methode`: nenne die Kandidaten aus dem Produktivcode, schlage einen vor und warte. Keine Datei anlegen.
2. Lies die Methode ganz und schreibe die Landkarte mit der Wertetabelle aus dem Skill als Text in deine Antwort, bevor du irgendeine Datei anlegst: Eingänge, Ausgänge, je Eingang die Literale mit Zeile und die Werte daraus.
3. Schreibe `src/test/java/<Package der Klasse>/<Klasse>GoldenMasterTest.java` nach dem Muster im Skill; die Testmethode heißt wie die Methode.
4. `mvn -q verify`. Der erste Lauf ist rot, neben der Klasse liegt `<Klasse>GoldenMasterTest.<methode>.received.txt`. Lies sie mit `cat` und halte je Kante die Zeile diesseits und jenseits gegen den Code. Stimmt sie, genehmige mit `scripts/approve.sh`.
5. `mvn -q verify` ist grün. Dann `scripts/unabgedeckt.sh <Klasse>`: nennt es Zeilen in der Methode oder in Methoden, die sie ruft, fehlt das Literal darin im Raster. Raster erweitern, `mvn -q verify`, lesen, `scripts/approve.sh`. Zeilen außerhalb der Methode (andere Methoden der Klasse, tote Helfer) bleiben unerreicht: kein Test dafür, keine Reflection; sie kommen als Beobachtung in die Antwort.

Regeln:
- Kein Produktivcode wird angefasst. Keine Tests löschen, kein `@Ignore`. Tests unter `approval/`, falls vorhanden, gehören zur Kata und bleiben.
- Kein Zufall. Jeder Wert im Raster hat sein Literal in der Wertetabelle.
- Erneut genehmigen nur, wenn das Raster sich geändert hat.
- Fertig ist erst, wenn `mvn -q verify` grün ist und die `.approved.txt` neben dem Test liegt.
- Am Ende: die Wertetabelle; Zeilen der genehmigten Datei; die Stichproben je Kante, also die Zeile diesseits und die Zeile jenseits wörtlich aus der Datei und die Codezeile, die sie unterscheidet; die Zeile „Zusammenfassung" aus `scripts/unabgedeckt.sh <Klasse>`; Beobachtungen; und in einem Satz, was dieses Netz nicht sagen kann.
