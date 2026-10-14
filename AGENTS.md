# Katas

Katas mit einem gemeinsamen Werkzeugkasten. Jede Kata ist ein Ordner mit eigener `pom.xml` und eigener AGENTS.md; opencode startet man im Kata-Ordner. Was hier steht, gilt in jedem Ordner.

## Befehle
- `mvn -q verify`: das einzige Fertig-Kriterium. Keine Ausgabe und Exit-Code 0 heißt grün. Schreibt den Abdeckungsbericht nach `target/site/jacoco/`.
- `../scripts/unabgedeckt.sh [Klasse]`: Zeilen und Verzweigungen, die die Tests nicht erreichen. Nach jedem `mvn -q verify` neu.
- `../scripts/approve.sh`: macht aus `*.received.txt` unter `src/test` die genehmigte `*.approved.txt` (ApprovalTests).
- `../scripts/schritt.sh "<Refactoring>: <was>"`: ein Refactoring-Schritt. `mvn -q verify`; grün committet `src/main`, rot setzt es zurück, mehr als 40 geänderte Zeilen lehnt es ab.
- `../scripts/regeln.sh [Dokument]`: nummeriert die Sätze des Anforderungsdokuments (`*Kata.md`) und zählt je Regel die Tests, die sie als `// R<n>` nennen.
- `java ../.opencode/skills/clean-code-report/CleanCodeReport.java` (oder `/clean-code-report`): Clean-Code-Report nach `target/clean-code-report.html`, Punkte und Smells je Methode.
- Verboten: Tests löschen oder mit `@Ignore` abschalten, `-DskipTests`, Änderungen an `pom.xml`, `opencode.json`, `.opencode/` und AGENTS.md. Ausnahme: die eigene Rollen-Datei (siehe „Dazulernen“).

## Rollen
| Agent | Tut | Ändert |
|---|---|---|
| `spec-und-akzeptanztestschreiber` | Specs aus Stories, Tests aus Specs, Sicherheitsnetz für Legacy-Code | `specs/`, `src/test/` |
| `clean-code-reviewer` | Befunde und Report | nichts |
| `refactorer` | Umbau in kleinen Schritten, ein Refactoring je Commit | `src/main/` über `schritt.sh` |

## Dazulernen
Jede Rolle darf ihre eigene Definition unter `.opencode/agents/<rolle>.md` ändern, und nur die. Was sie in einer Session lernt und beim nächsten Mal braucht, schreibt sie in den Abschnitt „Gelernt“ am Ende ihrer Datei: eine Zeile je Erkenntnis, mit Datum. So lernt die Rolle über Sessions hinweg. Fremde Definitionen und `opencode.json` sind für alle gesperrt.

## Arbeitsweise
- Ein Schritt je Runde: ein Test, ein Refactoring, ein Szenario.
- Erst der rote Test, dann der Code, dann `mvn -q verify`. Ein Test wird nie passend gemacht.
- Produktivcode bleibt unverändert, solange er nicht unter Test ist.
- Fertig ist erst, wenn `mvn -q verify` grün ist. Vorher nicht „fertig“ sagen.
- Am Ende drei Zeilen: geändert, Ergebnis von `mvn -q verify`, offen.

## Code-Regeln
- Java 17. Tests auf Englisch benannt, Name = Verhalten, ein Verhalten je Test.
- Hamcrest `assertThat(..., is(...))`. Abfragen stubben, Befehle verifizieren, Werte nie mocken.
- Methoden höchstens 20 Zeilen, eine Abstraktionsebene je Methode. Guard Clauses statt verschachtelter `if`.
