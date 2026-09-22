# Gilded Rose

## Befehle
- `mvn -q verify`: das einzige Fertig-Kriterium. Keine Ausgabe und Exit-Code 0 heißt grün. Schreibt den Abdeckungsbericht nach `target/site/jacoco/`.
- `scripts/unabgedeckt.sh GildedRose`: Zeilen und Verzweigungen von `GildedRose`, die die Tests nicht erreichen. Nach jedem `mvn -q verify` neu.
- `cat target/site/jacoco/jacoco.csv`: Zähler je Klasse, Spalten `BRANCH_MISSED` und `BRANCH_COVERED`.
- `scripts/approve.sh`: macht aus `*.received.txt` unter `src/test` die genehmigte `*.approved.txt` (ApprovalTests).
- `scripts/regeln.sh`: nummeriert die Sätze aus `GildedRoseKata.md` (R1 bis Rn) und zählt je Regel die Tests, die sie als `// R<n>` nennen; je Zahl im Satz, ob die Tests auch den Nachbarwert als Eingabe benutzen.
- `java .opencode/skills/clean-code-report/CleanCodeReport.java` (oder `/clean-code-report`): Clean-Code-Report nach `target/clean-code-report.html`, Punkte und Smells je Methode.
- Verboten: Tests löschen oder mit `@Ignore` abschalten, `-DskipTests`, Änderungen an `pom.xml`, `.opencode/`, `AGENTS.md`, `opencode.json`.

## Struktur
- `src/main/java/de/sharpsharp/gildedrose/GildedRose.java`: `updateQuality()` ist der Legacy-Code. `GildedRose.with(Item...)` baut den Laden.
- `Item.java`: Name, `sellIn`, `quality`, mit Gettern und Settern. Bleibt unverändert.
- `Main.java`: druckt einen Tag. Keine Regeln darin.
- `src/test/java/de/sharpsharp/gildedrose/`: JUnit 4, Hamcrest, Mockito, ApprovalTests.

## Fachliches
- Waren: gewöhnliche, „Aged Brie", „Backstage passes to a TAFKAL80ETC concert", „Sulfuras, Hand of Ragnaros". Die Namen stehen wörtlich im Code.
- Wie die Regeln sein sollten, steht in `GildedRoseKata.md`. Was der Code tut, entscheidet der Code.

## Arbeitsweise
- Drei Netze für den Umbau: `/approval-test` friert die Ausgabe über ein Raster ein (Regeln: `.opencode/skills/approval-test/SKILL.md`), `/characterization-test` hält je Verzweigung ein Verhalten mit Namen fest (Regeln: `.opencode/skills/characterization-test/SKILL.md`). Beide beschreiben, was der Code tut, nicht, was er tun sollte. `/spec-test` leitet aus `GildedRoseKata.md` ab, was der Code soll, ohne den Rumpf zu lesen (Regeln: `.opencode/skills/spec-test/SKILL.md`); rot ist dort ein Fund.
- Produktivcode bleibt unverändert, solange er nicht unter Test ist.
- Nach jedem Test `mvn -q verify`.
- Am Ende drei Zeilen: geändert, Ergebnis von `mvn -q verify` mit der Zusammenfassung aus `scripts/unabgedeckt.sh GildedRose`, Beobachtungen.

## Code-Regeln
- Java 17. Tests auf Englisch benannt, Name = beobachtetes Verhalten, ein Verhalten je Test.
- Hamcrest `assertThat(..., is(...))`. Keine Mocks für `Item` und `GildedRose`.
