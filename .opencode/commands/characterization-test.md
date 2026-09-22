---
description: Characterization Tests für GildedRose, bis jede Verzweigung und jeder Grenzwert unter Test ist
agent: test-autor
---
Bring das Verhalten von `GildedRose.updateQuality()` unter Test. Bereich: $ARGUMENTS (leer heißt: alles).

Skill characterization-test, hier eingefügt:
@.opencode/skills/characterization-test/SKILL.md

Der Legacy-Code:
@src/main/java/de/sharpsharp/gildedrose/GildedRose.java

Vorhandene Tests:
!`ls src/test/java/de/sharpsharp/gildedrose/`

Stand der Abdeckung (leer, wenn noch kein Bericht da ist; dann zuerst `mvn -q verify`):
!`scripts/unabgedeckt.sh GildedRose 2>/dev/null`

Vorgehen:
1. Lies `GildedRose.java` ganz und die vorhandenen Tests. Notiere die Warenarten und die Grenzwerte, die der Code unterscheidet: die Namen, `sellIn` 11 und 6 und 0, `quality` 0 und 50.
2. Führe `mvn -q verify` aus, dann `scripts/unabgedeckt.sh GildedRose`. Das ist deine Liste.
3. Je nicht erreichter Verzweigung ein Test in `src/test/java/de/sharpsharp/gildedrose/`: ein Item, ein Tag, beide Werte prüfen (`quality` und `sellIn`). Erst den erwarteten Wert hinschreiben, dann `mvn -q verify`. Ist der Test rot, prüfe, ob der Test das Richtige misst; wenn ja, übernimm den Ist-Wert und markiere die Stelle mit `// Beobachtung:` und einem Satz.
4. Wiederhole 2 und 3, bis `scripts/unabgedeckt.sh GildedRose` keine Zeile mehr nennt. Danach die Grenzwerte aus Regel 5 des Skills, auch wenn die Abdeckung schon voll ist.
5. Gruppiere die Tests je Warenart in eigene Klassen: `OrdinaryItemTest`, `AgedBrieTest`, `BackstagePassTest`, `SulfurasTest`. Namen wie im Skill. Der vorhandene Test aus `GildedRoseTest` zieht mit um; umziehen ist erlaubt, löschen nicht.

Regeln:
- Kein Produktivcode wird angefasst. Die Tests passen sich dem Code an, nie umgekehrt.
- Keine Tests löschen, kein `@Ignore`.
- Fertig ist erst, wenn `mvn -q verify` grün ist und `scripts/unabgedeckt.sh GildedRose` keine Zeile mehr nennt.
- Am Ende: Anzahl Tests, die Zeile „Zusammenfassung" aus `scripts/unabgedeckt.sh GildedRose`, Liste der Beobachtungen (Verhalten, das von `GildedRoseKata.md` abweicht oder überrascht).
