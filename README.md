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

Das Repo bringt opencode eine Rolle mit: `test-autor` schreibt Characterization Tests und darf nur unter
`src/test` schreiben. Nichts installieren: im Projektordner `opencode --agent test-autor` starten (oder in
IntelliJ den Session-Modus `test-autor` wählen), dann kennt es den Command und bleibt in der Rolle.

```text
/charakterisiere                     alles Verhalten von updateQuality unter Test bringen
/charakterisiere Backstage passes    nur diesen Bereich
```

| Datei | Wirkung |
|---|---|
| `AGENTS.md` | Befehle, Struktur, Regeln. Liest jede Rolle in jeder Session |
| `opencode.json` | die Rolle `test-autor` mit ihren Rechten, die Bash-Whitelist |
| `.opencode/commands/charakterisiere.md` | der Command; lesbares Markdown, das ist der Prompt |
| `.opencode/skills/characterization-tests/SKILL.md` | zwölf Regeln für Characterization Tests |
| `scripts/unabgedeckt.sh` | nicht erreichte Zeilen und Verzweigungen aus dem JaCoCo-Bericht |

| Rolle | Darf ändern | Bash |
|---|---|---|
| `test-autor` | nur `src/test/java/` | mvn, `scripts/unabgedeckt.sh`, ls, cat, grep, git status/diff/log |

## Struktur

| Ort | Inhalt |
|---|---|
| `src/main/java/de/sharpsharp/gildedrose/GildedRose.java` | der Legacy-Code |
| `src/main/java/de/sharpsharp/gildedrose/Item.java` | gehört dem Goblin, bleibt unverändert |
| `src/main/java/de/sharpsharp/gildedrose/Main.java` | ein Tag im Laden, druckt vorher und nachher |
| `src/test/java/de/sharpsharp/gildedrose/` | die Tests; ApprovalTests liegt für einen Golden Master bereit |
| `GildedRoseKata.md` | die Aufgabe |
