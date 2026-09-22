# Gilded Rose Kata

Legacy-Code ohne Tests: `GildedRose.updateQuality()` verändert jeden Tag Qualität und Restlaufzeit der Waren
im Laden. Die Aufgabe steht in `GildedRoseKata.md`: „Conjured"-Waren einbauen, ohne das bestehende Verhalten
zu ändern. Wer sicher umbauen will, bringt den Code zuerst unter Test.

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

Das Repo bringt opencode eine Rolle mit: `test-autor` schreibt Tests und darf nur unter `src/test` schreiben.
Drei Commands, drei Ansätze: Der Approval-Test ist Brute Force, er friert die Ausgabe über ein Raster aus
Eingaben in einer Datei ein. Der Characterization Test hält je Verzweigung ein Verhalten fest und gibt ihm
einen Namen. Beide beschreiben, was der Code tut. Der Spec-Test liest die Kata-Beschreibung statt den Code
und prüft, was der Code soll: je Satz Normalfall, Schwellen, Ränder und Kollisionen. Nichts installieren: im Projektordner `opencode --agent test-autor` starten (oder in IntelliJ
den Session-Modus `test-autor` wählen), dann kennt es die Commands und bleibt in der Rolle.

```text
/approval-test                              Golden Master: Raster aus Waren und Grenzwerten, genehmigt als Datei
/characterization-test                      je Verzweigung ein Test, Name = beobachtete Regel
/characterization-test Backstage passes     nur diesen Bereich
/generate-tests-from-spec                   Tests aus GildedRoseKata.md, der Code bleibt zu
/generate-tests-from-spec Conjured          nur die neue Anforderung; die Tests sind rot, das ist der Auftrag
```

| Datei | Wirkung |
|---|---|
| `AGENTS.md` | Befehle, Struktur, Regeln. Liest jede Rolle in jeder Session |
| `opencode.json` | die Rolle `test-autor` mit ihren Rechten, die Bash-Whitelist |
| `.opencode/commands/*.md` | `/approval-test`, `/characterization-test`; lesbares Markdown, das ist der Prompt |
| `.opencode/skills/approval-test/SKILL.md` | sieben Regeln und das Muster für den Golden Master |
| `.opencode/skills/characterization-test/SKILL.md` | zwölf Regeln für Characterization Tests |
| `.opencode/skills/generate-tests-from-spec/SKILL.md` | zwölf Regeln für Spec-Tests aus dem Anforderungsdokument, mit der Ableitung an einem Satz |
| `scripts/unabgedeckt.sh` | nicht erreichte Zeilen und Verzweigungen aus dem JaCoCo-Bericht |
| `scripts/approve.sh` | macht aus `*.received.txt` die genehmigte `*.approved.txt` |
| `scripts/regeln.sh` | nummeriert die Sätze aus `GildedRoseKata.md`, nennt Regeln ohne Test und Schwellen ohne Nachbar-Test |

| Rolle | Darf ändern | Bash |
|---|---|---|
| `test-autor` | nur `src/test/java/` | mvn, `scripts/unabgedeckt.sh`, `scripts/approve.sh`, `scripts/regeln.sh`, ls, cat, grep, git status/diff/log |

## Clean-Code-Report

Ein Blick auf den Code, ohne Gnade und ohne Gate: Der Bericht liest den Code mit dem Java-Parser des JDK,
sucht 30 Code Smells aus sechs Familien (Bloaters, Object-Orientation Abusers, Dispensables, Couplers,
Readability, Test Smells) und macht daraus Punkte, einen Rang, einen Radar je Familie und eine Monster-Galerie.
Links die Funde, rechts der Code wie in der IDE, ein Klick springt zur Zeile; das Regelwerk mit jeder Schwelle
und jedem Refactoring steht im Bericht.

```bash
java .opencode/skills/clean-code-report/CleanCodeReport.java   # schreibt target/clean-code-report.html
```

In opencode: `/clean-code-report` erzeugt den Bericht und nennt die drei teuersten Funde samt erstem Schritt.
Nichts zu installieren, JDK 17 reicht. Jeder Lauf merkt sich den Punktestand in `.clean-code-history`,
der Bericht zeigt den Verlauf. Der Skill ist ein Ordner: `.opencode/skills/clean-code-report/` plus der
Command lassen sich in jedes Java-Projekt kopieren.

## Struktur

| Ort | Inhalt |
|---|---|
| `src/main/java/de/sharpsharp/gildedrose/GildedRose.java` | der Legacy-Code |
| `src/main/java/de/sharpsharp/gildedrose/Item.java` | gehört dem Goblin, bleibt unverändert |
| `src/main/java/de/sharpsharp/gildedrose/Main.java` | ein Tag im Laden, druckt vorher und nachher |
| `src/test/java/de/sharpsharp/gildedrose/` | die Tests; ApprovalTests liegt für einen Golden Master bereit |
| `GildedRoseKata.md` | die Aufgabe |
