---
description: Review der Änderungen, Befunde mit Datei:Zeile, Smell und Vorschlag
agent: clean-code-reviewer
---
Prüfe die Änderungen in dieser Kata. $ARGUMENTS

Skill clean-code-check, hier eingefügt:
@../.opencode/skills/clean-code-check/SKILL.md

Geänderte Dateien seit dem letzten Commit:
!`git diff HEAD --stat -- src`

Der Diff:
!`git diff HEAD -- src`

Ist der Diff leer, prüfe stattdessen den letzten Commit dieser Kata mit `git log -1 -p -- .`.

Regeln:
- Lies die geänderten Dateien ganz, nicht nur den Diff.
- Prüfe jeden Punkt des Skills. Je Befund eine Zeile: `Datei:Zeile`, Name des Smells, Refactoring-Vorschlag in einem Satz.
- Prüfe zusätzlich: Testwerte im Produktivcode, Fachlogik außerhalb des Kerns (`Main`, Adapter), Tests, die nichts prüfen.
- Ändere nichts.
- Gibt es nichts Wesentliches, antworte genau: "Keine wesentlichen Befunde."
