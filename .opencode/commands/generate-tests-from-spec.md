---
description: Tests aus GildedRoseKata.md ableiten, je Regel Normalfall, Schwellen, Ränder und Kollisionen; der Code bleibt zu
agent: test-autor
---
Leite die Tests aus dem Anforderungsdokument ab. Bereich: $ARGUMENTS (leer heißt: das bestehende System, alle Regeln vor „This requires an update to our system". `Conjured` heißt: nur die neue Anforderung).

Skill generate-tests-from-spec, hier eingefügt:
@.opencode/skills/generate-tests-from-spec/SKILL.md

Das Anforderungsdokument:
@GildedRoseKata.md

Die Regeln, nummeriert, mit Zahlen und Signalwörtern je Satz und dem Stand der Tests:
!`scripts/regeln.sh`

Die Schnittstelle. Nur die Signaturen; den Rumpf von `GildedRose.java` liest du nicht:
!`grep -nE 'public' src/main/java/de/sharpsharp/gildedrose/GildedRose.java`
@src/main/java/de/sharpsharp/gildedrose/Item.java

Die Namen, die das System kennt (Textkonstanten aus dem Code; das Dokument kürzt sie ab, die Tests benutzen sie wörtlich; andere Waren heißen beliebig):
!`grep -ohE '"[^"]+"' src/main/java/de/sharpsharp/gildedrose/GildedRose.java | sort -u`

Vorhandene Tests:
!`ls src/test/java/de/sharpsharp/gildedrose/`

Vorgehen:
1. Regelkatalog: nimm die Nummern aus `scripts/regeln.sh`. Verhalten im Fließtext (die Klarstellung zu Sulfuras am Ende) bekommt die Nummer nach der letzten der Liste, also R11; R10 bleibt Conjured, auch wenn Conjured außerhalb des Bereichs liegt.
2. Ableitungstabelle nach dem Muster im Skill, eine Zeile je Regel im Bereich, Spalten Schwellen (auf / daneben), Ränder, Kollisionen, Offen. Jede Zelle ist gefüllt oder sagt „keine" mit Grund. Die Zahlen und Signalwörter aus `scripts/regeln.sh` sind die Kandidaten. Erst die Tabelle, dann die Tests.
3. Je Zelle die Tests, je Warenart eine Klasse `src/test/java/de/sharpsharp/gildedrose/<Warenart>SpecTest.java`: ein Item, ein Tag, beide Werte, über jedem Test `// R<n>: "<Zitat>"`. Muster im Skill.
4. Nach jeder Klasse `mvn -q verify`. Rot? Erst den Test gegen den Satz prüfen, dann den Satz auf Eindeutigkeit. Beides in Ordnung: der Test bleibt, wie er ist, die Abweichung kommt in die Antwort. Nie den Erwartungswert an den Code anpassen.
5. `scripts/regeln.sh`: nennt es im Bereich eine Regel ohne Test oder eine Schwelle ohne Nachbarn, zurück zu 3.
6. Checkliste des Skills als Tabelle: eine Zeile je Regel im Bereich, eine Spalte je Punkt 1 bis 6, in jeder Zelle ja oder nein. Ein nein bei 1 bis 4: zurück zu 3. Ein nein bei 5: Offene Frage in die Antwort.

Regeln:
- Kein Blick in den Rumpf von `GildedRose.java`: kein `cat`, kein `grep`, kein `head`. Auch nicht bei Rot.
- Kein Produktivcode wird angefasst. Keine Tests löschen, kein `@Ignore`.
- Fertig, Bereich leer: `scripts/regeln.sh` nennt keine Regel des bestehenden Systems ohne Test und keine Schwelle ohne Nachbarn, und `mvn -q verify` ist grün oder jeder rote Test steht mit Regel und Ursache in der Antwort.
- Fertig, Bereich Conjured: die Tests kompilieren und sind rot. Das ist der Auftrag für den Umbau; grün wird `mvn -q verify` erst, wenn jemand die Regel baut.
- Am Ende: die Ableitungstabelle, je Zelle die Testnamen und grün oder rot; die Checkliste als Tabelle Regel mal Punkt 1 bis 6, bei nein ein Wort Grund; Annahmen mit Zitat; Offene Fragen; ein Satz, was dieses Netz nicht sagen kann.
