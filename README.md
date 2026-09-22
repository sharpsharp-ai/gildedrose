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
Zwei Commands, zwei Ansätze: Der Approval-Test ist Brute Force, er friert die Ausgabe über ein Raster aus
Eingaben in einer Datei ein. Der Characterization Test hält je Verzweigung ein Verhalten fest und gibt ihm
einen Namen. Nichts installieren: im Projektordner `opencode --agent test-autor` starten (oder in IntelliJ
den Session-Modus `test-autor` wählen), dann kennt es die Commands und bleibt in der Rolle.

```text
/approval-test                              Golden Master: Raster aus Waren und Grenzwerten, genehmigt als Datei
/characterization-test                      je Verzweigung ein Test, Name = beobachtete Regel
/characterization-test Backstage passes     nur diesen Bereich
```

| Datei | Wirkung |
|---|---|
| `AGENTS.md` | Befehle, Struktur, Regeln. Liest jede Rolle in jeder Session |
| `opencode.json` | die Rolle `test-autor` mit ihren Rechten, die Bash-Whitelist |
| `.opencode/commands/*.md` | `/approval-test`, `/characterization-test`; lesbares Markdown, das ist der Prompt |
| `.opencode/skills/approval-test/SKILL.md` | sieben Regeln und das Muster für den Golden Master |
| `.opencode/skills/characterization-test/SKILL.md` | zwölf Regeln für Characterization Tests |
| `scripts/unabgedeckt.sh` | nicht erreichte Zeilen und Verzweigungen aus dem JaCoCo-Bericht |
| `scripts/approve.sh` | macht aus `*.received.txt` die genehmigte `*.approved.txt` |

| Rolle | Darf ändern | Bash |
|---|---|---|
| `test-autor` | nur `src/test/java/` | mvn, `scripts/unabgedeckt.sh`, `scripts/approve.sh`, ls, cat, grep, git status/diff/log |

## Struktur

| Ort | Inhalt |
|---|---|
| `src/main/java/de/sharpsharp/gildedrose/GildedRose.java` | der Legacy-Code |
| `src/main/java/de/sharpsharp/gildedrose/Item.java` | gehört dem Goblin, bleibt unverändert |
| `src/main/java/de/sharpsharp/gildedrose/Main.java` | ein Tag im Laden, druckt vorher und nachher |
| `src/test/java/de/sharpsharp/gildedrose/` | die Tests; ApprovalTests liegt für einen Golden Master bereit |
| `GildedRoseKata.md` | die Aufgabe |
