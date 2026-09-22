---
name: characterization-tests
description: Zwölf Regeln für Characterization Tests, die Legacy-Code so festhalten, wie er ist, damit man ihn gefahrlos umbauen kann
---
# Characterization Tests

Ein Characterization Test hält fest, was der Code tut. Nicht, was er tun sollte. Er ist das Netz für den Umbau.

| Nr | Regel | Warum |
|---|---|---|
| 1 | Erst raten, dann messen: Erwartung hinschreiben, laufen lassen, bei Rot den Ist-Wert übernehmen und mit `// Beobachtung:` markieren | Der Code ist die Wahrheit; die Überraschung ist der Wert des Tests |
| 2 | Ein Item, ein Tag, ein Verhalten je Test | Bricht ein Test, sagt sein Name, was sich geändert hat |
| 3 | Name = beobachtetes Verhalten: `agedBrieGainsTwoQualityAfterSellDate`, nie `test1` | Die Tests sind die Doku des Legacy-Codes |
| 4 | Beide Werte prüfen, `quality` und `sellIn`, auch wenn nur einer interessant scheint | Nebenwirkungen fallen sonst durch |
| 5 | Grenzwerte explizit: `sellIn` 11 und 10, 6 und 5, 1 und 0, -1; `quality` 0 und 1, 49 und 50; Sulfuras mit 80 | Legacy-Code lebt von `<` und `<=` |
| 6 | Mehrere Tage nur, wenn ein Tag nicht reicht, etwa beim Übergang über das Verfallsdatum | Sonst prüft ein Test viele Regeln auf einmal |
| 7 | Abdeckung messen, nicht schätzen: `mvn -q verify`, dann `scripts/unabgedeckt.sh GildedRose` | Jede nicht erreichte Zeile ist ein fehlender Test |
| 8 | Volle Abdeckung ist nicht das Ende: Regel 5 gilt auch für Zweige, die schon erreicht sind | Abdeckung sagt „erreicht", nicht „geprüft" |
| 9 | Deterministisch: keine Mocks, kein Zufall, keine Uhr | Der Test muss morgen dasselbe sagen |
| 10 | Produktivcode bleibt unverändert, auch kein `private` wird für einen Test geöffnet | Erst das Netz, dann der Umbau |
| 11 | Fehler nicht reparieren, sondern festhalten: Ist-Wert plus `// Beobachtung:` | Ob die Regel geändert wird, entscheidet später jemand bewusst |
| 12 | Nach jedem Test `mvn -q verify`; nie mehr als ein roter Test gleichzeitig | Kleine Schritte, klare Ursache |

## Golden Master, optional als zweites Netz
`Approvals.verifyAll("", items)` aus ApprovalTests druckt viele Items nach einem Tag in eine Datei. Beim ersten Lauf entsteht `*.received.txt` und der Test ist rot; die Datei wird mit gleichem Inhalt als `*.approved.txt` neben den Test gelegt, dann ist er grün. Breites Netz aus Kombinationen (Warenart × `sellIn` × `quality`), grob, aber schnell. Die feinen Tests bleiben, weil sie lesbar sind.

## Fertig
- `scripts/unabgedeckt.sh GildedRose` nennt keine Zeile mehr.
- Jeder Grenzwert aus Regel 5 hat einen Test.
- `mvn -q verify` grün.
- Eine Liste der Beobachtungen.
