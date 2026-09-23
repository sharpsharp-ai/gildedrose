# Gilded Rose

## Befehle
- `mvn -q verify`: das einzige Fertig-Kriterium. Keine Ausgabe und Exit-Code 0 heißt grün. Schreibt den Abdeckungsbericht nach `target/site/jacoco/`.
- `scripts/unabgedeckt.sh GildedRose`: Zeilen und Verzweigungen von `GildedRose`, die die Tests nicht erreichen. Nach jedem `mvn -q verify` neu.
- `cat target/site/jacoco/jacoco.csv`: Zähler je Klasse, Spalten `BRANCH_MISSED` und `BRANCH_COVERED`.
- `scripts/approve.sh`: macht aus `*.received.txt` unter `src/test` die genehmigte `*.approved.txt` (ApprovalTests).
- `scripts/schritt.sh "<Refactoring>: <was>"`: ein Refactoring-Schritt. `mvn -q verify`; grün committet `src/main` mit der Botschaft, rot setzt `src/main` auf den letzten Commit zurück, mehr als 40 geänderte Zeilen lehnt es ab.
- `scripts/regeln.sh`: nummeriert die Sätze aus `GildedRoseKata.md` (R1 bis Rn) und zählt je Regel die Tests, die sie als `// R<n>` nennen; je Zahl im Satz, ob die Tests auch den Nachbarwert als Eingabe benutzen.
- `java .opencode/skills/clean-code-report/CleanCodeReport.java` (oder `/clean-code-report`): Clean-Code-Report nach `target/clean-code-report.html`, Punkte und Smells je Methode.
- Verboten: Tests löschen oder mit `@Ignore` abschalten, `-DskipTests`, Änderungen an `pom.xml`, `.opencode/`, `AGENTS.md`, `opencode.json`.

## Struktur
- `src/main/java/de/sharpsharp/gildedrose/GildedRose.java`: `updateQuality()` ist der Legacy-Code. `GildedRose.with(Item...)` baut den Laden.
- `Item.java`: Name, `sellIn`, `quality`, mit Gettern und Settern. Bleibt unverändert.
- `Main.java`: druckt einen Tag. Keine Regeln darin.
- `src/main/java/de/sharpsharp/gildedrose/smells/`: fünf Übungsbeispiele (Primitive Obsession, Inappropriate Intimacy, Long Parameter List, Shotgun Surgery, Feature Envy), absichtlich schlecht, Rezept im Klassenkommentar, Tests unter `src/test/java/de/sharpsharp/gildedrose/smells/`. Gehören nicht zur Kata: nicht anfassen, wenn der Auftrag `GildedRose` heißt.
- `src/test/java/de/sharpsharp/gildedrose/`: JUnit 4, Hamcrest, Mockito, ApprovalTests. 25 Tests aus der Original-Kata, je Warenart eine Klasse (`AGildedRoseItem`, `AnAgedBrie`, `ABackstagePass`, `TheHandOfRagnaros`), Namen und Grenzen in `TestConstants`; sie erreichen jede Zeile und jeden Zweig von `GildedRose`. Surefire führt jede Klasse unter `src/test` aus, der Name muss nicht auf `Test` enden.

## Fachliches
- Waren: gewöhnliche, „Aged Brie", „Backstage passes to a TAFKAL80ETC concert", „Sulfuras, Hand of Ragnaros". Die Namen stehen wörtlich im Code.
- Wie die Regeln sein sollten, steht in `GildedRoseKata.md`. Was der Code tut, entscheidet der Code.

## Arbeitsweise
- Drei Netze für den Umbau: `/approval-test` friert die Ausgabe über ein Raster ein (Regeln: `.opencode/skills/approval-test/SKILL.md`), `/characterization-test` hält je Verzweigung ein Verhalten mit Namen fest (Regeln: `.opencode/skills/characterization-test/SKILL.md`). Beide beschreiben, was der Code tut, nicht, was er tun sollte. `/generate-tests-from-spec` leitet aus `GildedRoseKata.md` ab, was der Code soll, ohne den Rumpf zu lesen (Regeln: `.opencode/skills/generate-tests-from-spec/SKILL.md`); rot ist dort ein Fund.
- Umbau nur mit Netz und nur über `/refactor-in-small-steps` (Rolle `refactorer`, Regeln: `.opencode/skills/refactor-in-small-steps/SKILL.md`): ein Refactoring je Commit über `scripts/schritt.sh`, rot heißt zurück, Tests bleiben unverändert.
- Produktivcode bleibt unverändert, solange er nicht unter Test ist.
- Nach jedem Test `mvn -q verify`.
- Am Ende drei Zeilen: geändert, Ergebnis von `mvn -q verify` mit der Zusammenfassung aus `scripts/unabgedeckt.sh GildedRose`, Beobachtungen.

## Code-Regeln
- Java 17. Tests auf Englisch benannt, Name = beobachtetes Verhalten, ein Verhalten je Test.
- Hamcrest `assertThat(..., is(...))`. Keine Mocks für `Item` und `GildedRose`.
