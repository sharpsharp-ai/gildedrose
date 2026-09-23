# Gilded Rose Kata

Legacy-Code mit Netz: `GildedRose.updateQuality()` verändert jeden Tag Qualität und Restlaufzeit der Waren
im Laden. 25 Tests aus der Original-Kata, je Warenart eine Klasse, erreichen jede Zeile und jeden Zweig und sagen,
was der Code tut. Die Aufgabe steht in `GildedRoseKata.md`: „Conjured"-Waren einbauen, ohne das bestehende Verhalten
zu ändern. Wer das sauber machen will, baut vorher um, und das Netz sagt nach jedem Schritt, ob noch alles tut, was es tat.

Fertig ist eine Änderung, wenn `mvn -q verify` ohne Ausgabe und mit Exit-Code 0 endet.

## Loslegen

Voraussetzung: JDK 17 oder neuer und Maven (IntelliJ bringt Maven mit).

```bash
git clone https://github.com/sharpsharp-ai/gildedrose.git
cd gildedrose
mvn -q verify                     # keine Ausgabe heißt grün; schreibt den Abdeckungsbericht
scripts/unabgedeckt.sh GildedRose # welche Zeilen und Verzweigungen die Tests nicht erreichen
mvn -q compile exec:java          # ein Tag im Laden, vorher und nachher
```

IntelliJ: File → New → Project from Version Control, die URL einfügen. IntelliJ erkennt die `pom.xml`
und lädt die Bibliotheken. Rechtsklick auf `src/test/java` → Run 'All Tests'.

## Mit opencode arbeiten

Das Repo bringt opencode zwei Rollen mit: `test-autor` schreibt Tests und darf nur unter `src/test` schreiben,
`refactorer` baut um und darf nur unter `src/main` schreiben. Die 25 Tests sind ein Netz, aber eines aus dem Soll-Raum:
Sulfuras kennen sie nur mit Qualität 80, Grenzwerte nur dort, wo die Regeln sie nennen. Drei Commands ergänzen es, drei Ansätze: Der Approval-Test ist Brute Force, er friert die Ausgabe über ein Raster aus
Eingaben in einer Datei ein. Der Characterization Test hält je Verzweigung ein Verhalten fest und gibt ihm
einen Namen. Beide beschreiben, was der Code tut. Der Spec-Test liest die Kata-Beschreibung statt den Code
und prüft, was der Code soll: je Satz Normalfall, Schwellen, Ränder und Kollisionen. Steht das Netz, baut die Rolle
`refactorer` um: ein Refactoring je Commit, `scripts/schritt.sh` committet grün und setzt rot zurück. Nichts installieren: im Projektordner `opencode --agent test-autor` starten (oder in IntelliJ
den Session-Modus `test-autor` wählen), dann kennt es die Commands und bleibt in der Rolle.

```text
/approval-test                              Golden Master: Raster aus Waren und Grenzwerten, genehmigt als Datei
/characterization-test                      je Verzweigung ein Test, Name = beobachtete Regel
/characterization-test Backstage passes     nur diesen Bereich
/generate-tests-from-spec                   Tests aus GildedRoseKata.md, der Code bleibt zu
/generate-tests-from-spec Conjured          nur die neue Anforderung; die Tests sind rot, das ist der Auftrag
/refactor-in-small-steps                    Umbau unter Test, ein Refactoring je Commit, rot heißt zurück
/refactor-in-small-steps Methoden unter 10 Zeilen   mit eigenem Ziel
```

| Datei | Wirkung |
|---|---|
| `AGENTS.md` | Befehle, Struktur, Regeln. Liest jede Rolle in jeder Session |
| `opencode.json` | die Rolle `test-autor` mit ihren Rechten, die Bash-Whitelist |
| `.opencode/commands/*.md` | `/approval-test`, `/characterization-test`; lesbares Markdown, das ist der Prompt |
| `.opencode/skills/approval-test/SKILL.md` | sieben Regeln und das Muster für den Golden Master |
| `.opencode/skills/characterization-test/SKILL.md` | zwölf Regeln für Characterization Tests |
| `.opencode/skills/generate-tests-from-spec/SKILL.md` | zwölf Regeln für Spec-Tests aus dem Anforderungsdokument, mit der Ableitung an einem Satz |
| `.opencode/skills/refactor-in-small-steps/SKILL.md` | zehn Regeln und der Katalog fürs Refactoring in kleinen Schritten |
| `scripts/unabgedeckt.sh` | nicht erreichte Zeilen und Verzweigungen aus dem JaCoCo-Bericht |
| `scripts/approve.sh` | macht aus `*.received.txt` die genehmigte `*.approved.txt` |
| `scripts/regeln.sh` | nummeriert die Sätze aus `GildedRoseKata.md`, nennt Regeln ohne Test und Schwellen ohne Nachbar-Test |
| `scripts/schritt.sh` | ein Refactoring-Schritt: `mvn -q verify`, grün committet `src/main`, rot setzt es zurück, über 40 Zeilen lehnt es ab |

| Rolle | Darf ändern | Bash |
|---|---|---|
| `test-autor` | nur `src/test/java/` | mvn, `scripts/unabgedeckt.sh`, `scripts/approve.sh`, `scripts/regeln.sh`, ls, cat, grep, git status/diff/log |
| `refactorer` | nur `src/main/java/` | wie oben, dazu `scripts/schritt.sh`; `scripts/approve.sh` gesperrt, git schreibend nur über das Skript |

## Clean-Code-Report

Ein Blick auf den Code, ohne Gnade und ohne Gate: Der Bericht liest den Code mit dem Java-Parser des JDK,
sucht 30 Code Smells aus sechs Familien (Bloaters, Object-Orientation Abusers, Dispensables, Couplers,
Readability, Test Smells) und macht daraus Punkte, einen Rang, einen Radar je Familie und eine Monster-Galerie.
Links die Funde, rechts der Code wie in der IDE, ein Klick springt zur Zeile; das Regelwerk mit jeder Schwelle
und jedem Refactoring steht im Bericht. Ein Ausschnitt geht auch: `--nur core`, `--nur Kasse` oder `--nur Kasse#bezahlen`,
in opencode auch in Worten: `/clean-code-report nur die Domain-Klassen`.

```bash
java .opencode/skills/clean-code-report/CleanCodeReport.java   # schreibt target/clean-code-report.html
```

In opencode: `/clean-code-report` erzeugt den Bericht und nennt die drei teuersten Funde samt erstem Schritt.
Nichts zu installieren, JDK 17 reicht. Jeder Lauf merkt sich den Punktestand in `.clean-code-history`,
der Bericht zeigt den Verlauf. Der Skill ist ein Ordner: `.opencode/skills/clean-code-report/` plus der
Command lassen sich in jedes Java-Projekt kopieren.

## Fünf Smells zum Üben

Unter `src/main/java/de/sharpsharp/gildedrose/smells/` liegt je Smell ein kleines Beispiel aus dem Laden, mit Test.
Das Rezept steht als Kommentar über der Klasse, die den Smell trägt: woran man ihn erkennt, wohin es gehen soll
und welche IntelliJ-Refactorings den Weg gehen, mit Shortcuts für Mac und Windows.

| Smell | Package | Klasse mit dem Rezept | Hauptwerkzeug in IntelliJ |
|---|---|---|---|
| Primitive Obsession | `primitiveobsession` | `Supplier` | Extract Delegate, Convert To Instance Method, Convert to record |
| Inappropriate Intimacy | `inappropriateintimacy` | `Shelf` | Extract Method, Move Instance Method, Encapsulate Fields |
| Long Parameter List | `longparameterlist` | `Ledger` | Introduce Parameter Object, Change Signature |
| Shotgun Surgery | `shotgunsurgery` | `PriceTag` | Extract Constant, Move Members, Convert To Instance Method |
| Feature Envy | `featureenvy` | `Cashier` | Move Instance Method (F6) |

`/clean-code-report nur das Package smells` zeigt, was der Report davon sieht; Shotgun Surgery und Inappropriate
Intimacy misst er nicht. Im Bericht über das ganze Projekt fehlen die Beispiele (`.clean-code-ignore`), damit die Kata
die Kata bleibt. Der Branch `smells-geloest` enthält den Zustand nach den Rezepten. Die Tests sind dieselben, bis auf
die Aufrufe, die IntelliJ bei einer Signaturänderung mit umschreibt.

## Struktur

| Ort | Inhalt |
|---|---|
| `src/main/java/de/sharpsharp/gildedrose/GildedRose.java` | der Legacy-Code |
| `src/main/java/de/sharpsharp/gildedrose/Item.java` | gehört dem Goblin, bleibt unverändert |
| `src/main/java/de/sharpsharp/gildedrose/Main.java` | ein Tag im Laden, druckt vorher und nachher |
| `src/test/java/de/sharpsharp/gildedrose/` | 25 Tests aus der Original-Kata: `AGildedRoseItem`, `AnAgedBrie`, `ABackstagePass`, `TheHandOfRagnaros`, dazu `AItem` und `TestConstants`; ApprovalTests liegt für einen Golden Master bereit |
| `GildedRoseKata.md` | die Aufgabe |
