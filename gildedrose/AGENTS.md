# Gilded Rose

Die Regeln der Wurzel gelten (`../AGENTS.md`). Hier nur, was diese Kata besonders macht.

## Struktur
- `src/main/java/de/sharpsharp/gildedrose/GildedRose.java`: `updateQuality()` ist der Legacy-Code. `GildedRose.with(Item...)` baut den Laden.
- `Item.java`: Name, `sellIn`, `quality`, mit Gettern und Settern. Gehört dem Goblin, bleibt unverändert.
- `Main.java`: druckt einen Tag (`mvn -q compile exec:java`). Keine Regeln darin.
- `src/test/java/de/sharpsharp/gildedrose/`: das Sicherheitsnetz, die 25 Tests der Original-Kata, je Warenart eine Klasse (`AGildedRoseItem`, `AnAgedBrie`, `ABackstagePass`, `TheHandOfRagnaros`), Namen und Grenzen in `TestConstants`. Sie erreichen jede Zeile und jeden Zweig von `GildedRose`. Die Klassen heißen nach dem Gegenstand, nicht `*Test`; Surefire führt jede Klasse aus.
- `src/test/java/de/sharpsharp/gildedrose/approval/`: die zwei Approval-Tests der Original-Kata mit genehmigten Dateien, wie im Original mit `@Ignore`. Das `@Ignore` dort ist Vorgabe, nicht Verstoß.
- `GildedRoseKata.md`: die Anforderungen. Wie die Regeln sein sollten, steht dort; was der Code tut, entscheidet der Code.

## Der Weg
- Ein Sicherheitsnetz ist da. Weitere: `/approval-test GildedRose#updateQuality`, `/characterization-test GildedRose#updateQuality`, `/generate-tests-from-spec`. Drei Sicherheitsnetze, drei Fragen: was der Code tut, warum, und was er soll.
- Umbauen: `/refactor-in-small-steps GildedRose#updateQuality`, ein Refactoring je Commit.
- Dann die Aufgabe aus `GildedRoseKata.md`: „Conjured"-Waren, ohne das Bestehende zu ändern. `/generate-tests-from-spec Conjured` liefert die roten Tests als Auftrag.
