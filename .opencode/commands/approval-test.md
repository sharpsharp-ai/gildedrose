---
description: Golden Master für GildedRose mit ApprovalTests, brute force über alle Warenarten und Grenzwerte
agent: test-autor
---
Leg ein Golden Master über `GildedRose.updateQuality()` an. $ARGUMENTS

Skill approval-test, hier eingefügt:
@.opencode/skills/approval-test/SKILL.md

Der Legacy-Code:
@src/main/java/de/sharpsharp/gildedrose/GildedRose.java

Vorhandene Tests und genehmigte Dateien:
!`ls src/test/java/de/sharpsharp/gildedrose/`

Vorgehen:
1. Schreibe `src/test/java/de/sharpsharp/gildedrose/GildedRoseGoldenMasterTest.java` nach dem Muster im Skill: das Raster aus Namen × `sellIn` × `quality`, ein Tag, `Approvals.verifyAll("items", items)`.
2. `mvn -q verify`. Der erste Lauf ist rot, und neben der Klasse liegt jetzt `GildedRoseGoldenMasterTest.oneDay.received.txt`.
3. Lies die Datei mit `cat` und prüfe Stichproben gegen den Code: Sulfuras bleibt bei 80, Backstage nach dem Konzert auf 0, nichts über 50 außer Sulfuras. Sieht sie plausibel aus, genehmige mit `scripts/approve.sh`.
4. `mvn -q verify` ist grün. Dann `scripts/unabgedeckt.sh GildedRose`: nennt es noch Zeilen, erweitere das Raster, lass die Datei neu entstehen und genehmige erneut.

Regeln:
- Kein Produktivcode wird angefasst, auch `Item.toString()` nicht.
- Kein Zufall. Das Raster ist die Liste im Skill.
- Erneut genehmigen nur, wenn das Raster sich geändert hat.
- Fertig ist erst, wenn `mvn -q verify` grün ist und die `.approved.txt` neben dem Test liegt.
- Am Ende: Zeilen der genehmigten Datei, die Zeile „Zusammenfassung" aus `scripts/unabgedeckt.sh GildedRose`, und in einem Satz, was dieses Netz nicht sagen kann.
