---
description: Eine Methode in kleinen, grünen Schritten umbauen, ein Refactoring je Commit, rot heißt zurück
agent: refactorer
---
Baue um. Auftrag: $ARGUMENTS (Klasse#methode und Ziel. Ohne Ziel heißt: keine Methode länger als 20 Zeilen, keine Verschachtelung tiefer als 3, Verhalten unverändert. Ohne Methode: frag, welche, und nenne die Kandidaten aus dem Report).

Skill refactor-in-small-steps, hier eingefügt:
@../.opencode/skills/refactor-in-small-steps/SKILL.md

Produktivcode:
!`find src/main -name '*.java' | sort`

Das Netz. Erste Zeile leer heißt grün; nennt die Abdeckung noch viele Zeilen, ist das Netz zu dünn:
!`mvn -q verify 2>&1 | grep -E 'Tests run:.*Fail|FAIL|ERROR' | head -3`
!`../scripts/unabgedeckt.sh 2>/dev/null | grep -E '^==|Zusammenfassung'`
!`find src/test -name '*.java' | sort`

Arbeitsbaum dieser Kata, leer heißt sauber:
!`git status --short -- .`

Stand vorher:
!`git log --oneline -3 -- .`
!`java ../.opencode/skills/clean-code-report/CleanCodeReport.java 2>/dev/null | head -2`

Vorgehen:
1. Ist das Netz rot, der Arbeitsbaum nicht sauber oder sind mehr als eine Handvoll Zeilen der Klasse nicht abgedeckt: Stopp, sag warum, kein Umbau. Das Netz baut `/approval-test` oder `/characterization-test`.
2. Ein Refactoring aus dem Katalog wählen, benennen, nur das tun. Beginne mit dem Endgegner aus dem Report.
3. `../scripts/schritt.sh "<Refactoring>: <was>"`. Grün: weiter mit 2. Rot: der Code ist zurück auf dem letzten Commit, derselbe Schritt kleiner oder ein anderer. Zu groß: teilen.
4. Aufhören, wenn das Ziel erreicht ist oder der nächste Schritt das Verhalten ändern müsste.

Regeln:
- Kein Test wird angefasst, kein `../scripts/approve.sh`, kein `git add`, `git commit` oder `git checkout` außer über `../scripts/schritt.sh`.
- Verhalten bleibt, auch falsches: Beobachtungen in die Antwort, nicht in den Code.
- Am Ende: `git log --oneline` seit dem Start, Report-Punkte vorher und nachher (`java ../.opencode/skills/clean-code-report/CleanCodeReport.java` noch einmal), Ziel erreicht ja oder nein, Beobachtungen.
