---
description: Tests aus dem Anforderungsdokument der Kata ableiten, je Satz Normalfall, Schwellen, Ränder und Kollisionen; der Code bleibt zu
agent: spec-und-akzeptanztestschreiber
---
Leite die Tests aus dem Anforderungsdokument ab. Bereich: $ARGUMENTS (leer heißt: alle Regeln des Dokuments; sonst nur der genannte Ausschnitt, etwa eine Warenart, eine Regel oder eine neue Anforderung).

Skill generate-tests-from-spec, hier eingefügt:
@../.opencode/skills/generate-tests-from-spec/SKILL.md

Das Anforderungsdokument, die `*Kata.md` im Ordner. Ist keine da, sag das und hör auf:
!`cat *Kata.md 2>/dev/null`

Die Regeln, nummeriert, mit Zahlen und Signalwörtern je Satz und dem Stand der Tests. Liefert das Skript nichts (Dokument ohne Aufzählung), nummeriere die Sätze selbst nach dem Skill:
!`../scripts/regeln.sh 2>/dev/null`

Die Schnittstelle. Nur die Signaturen; den Rumpf der Klassen liest du nicht:
!`grep -rnE '^\s*public' src/main/java --include='*.java'`

Die Namen, die das System kennt (Textkonstanten aus dem Code; das Dokument kürzt sie ab, die Tests benutzen sie wörtlich):
!`grep -rohE '"[^"]+"' src/main/java --include='*.java' | sort -u`

Vorhandene Tests:
!`find src/test -name '*.java' | sort`

Vorgehen:
1. Regelkatalog: nimm die Nummern aus `../scripts/regeln.sh`. Verhalten im Fließtext bekommt die Nummer nach der letzten der Liste.
2. Ableitungstabelle nach dem Muster im Skill, eine Zeile je Regel im Bereich, Spalten Schwellen (auf / daneben), Ränder, Kollisionen, Offen. Jede Zelle ist gefüllt oder sagt „keine" mit Grund. Die Zahlen und Signalwörter aus dem Skript sind die Kandidaten. Erst die Tabelle, dann die Tests.
3. Je Zelle die Tests, eine Klasse je Gegenstand des Dokuments unter `src/test/java/<Package>/<Gegenstand>SpecTest.java`: ein Objekt, eine Aktion, alle Werte prüfen, über jedem Test `// R<n>: "<Zitat>"`. Muster im Skill.
4. Nach jeder Klasse `mvn -q verify`. Rot? Erst den Test gegen den Satz prüfen, dann den Satz auf Eindeutigkeit. Beides in Ordnung: der Test bleibt, wie er ist, die Abweichung kommt in die Antwort. Nie den Erwartungswert an den Code anpassen.
5. `../scripts/regeln.sh`: nennt es im Bereich eine Regel ohne Test oder eine Schwelle ohne Nachbarn, zurück zu 3.
6. Checkliste des Skills als Tabelle: eine Zeile je Regel im Bereich, eine Spalte je Punkt 1 bis 6, in jeder Zelle ja oder nein. Ein nein bei 1 bis 4: zurück zu 3. Ein nein bei 5: Offene Frage in die Antwort.

Regeln:
- Kein Blick in den Rumpf der Produktivklassen: kein `cat`, kein `grep` über den Rumpf, kein `head`. Auch nicht bei Rot.
- Kein Produktivcode wird angefasst. Keine Tests löschen, kein `@Ignore`.
- Fertig: `../scripts/regeln.sh` nennt im Bereich keine Regel ohne Test und keine Schwelle ohne Nachbarn, und `mvn -q verify` ist grün oder jeder rote Test steht mit Regel und Ursache in der Antwort. Beschreibt der Bereich etwas, das der Code noch nicht kann, sind die roten Tests der Auftrag für den Umbau.
- Am Ende: die Ableitungstabelle, je Zelle die Testnamen und grün oder rot; die Checkliste als Tabelle Regel mal Punkt 1 bis 6, bei nein ein Wort Grund; Annahmen mit Zitat; Offene Fragen; ein Satz, was dieses Netz nicht sagen kann.
