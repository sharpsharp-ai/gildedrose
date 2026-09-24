---
name: characterization-test
description: Eine Methode mit Characterization Tests Schicht für Schicht unter Test bringen, Verzweigung für Verzweigung, Grenzwert für Grenzwert, damit Legacy-Code so festgehalten ist, wie er ist, und gefahrlos umgebaut werden kann
---
# Characterization Tests, eine Methode Schicht für Schicht

Ein Characterization Test hält fest, was der Code tut. Nicht, was er tun sollte. Er ist das Netz für den Umbau (Michael Feathers). Das Verfahren gilt für jede Methode in jeder Sprache: Die Methode ist die Landkarte, jede Verzweigung ist eine Stelle zum Graben.

## Vorher: die Methode
Ist keine Methode genannt (`Klasse#methode`), frag danach und warte auf die Antwort. Nenne dabei die Kandidaten, die du im Produktivcode siehst, aber wähle nicht selbst. Ist nur eine Klasse genannt, frag nach der Methode.

Dann lies die Methode ganz, samt der privaten Methoden, die sie ruft, und zeichne die Landkarte in die Antwort, bevor der erste Test entsteht:
- Eingänge: Parameter, Felder, Abhängigkeiten, die die Methode liest.
- Ausgänge: Rückgabewert, veränderte Parameter, veränderte Felder, Ausnahmen, Aufrufe an Abhängigkeiten, Ausgaben.
- Verzweigungen, von oben nach unten nummeriert: jedes `if`, `else`, `switch`, `?:`, jede Schleifenbedingung, jedes frühe `return`, jeder `throw`. Bei jeder Verzweigung die Vergleichswerte, die sie unterscheidet.

## Graben
| Schicht | Was | Warum |
|---|---|---|
| 1 | Geradeaus: der einfachste Eingang, der ohne Sonderfall durch die Methode läuft. Erwartung raten, laufen lassen, bei Rot den Ist-Wert übernehmen und mit `// Beobachtung:` markieren | Der Code ist die Wahrheit; die Überraschung ist der Wert des Tests |
| 2 | Die erste Verzweigung, die Test 1 nicht genommen hat: ein Test, der die andere Seite nimmt. Derselbe Eingang wie vorher, nur das geändert, was die Verzweigung kippt | Zwei Nachbartests unterscheiden sich in genau einer Sache; bricht einer, ist die Ursache klar |
| 3 | Grenzwerte als Paar: für `x < n` und `x <= n` je ein Test mit dem letzten Wert diesseits und dem ersten jenseits der Kante, also n-1 und n oder n und n+1 | Legacy-Code lebt von `<` und `<=`; ein Test in der Mitte sieht die Kante nicht |
| 4 | Schleifen: kein Element, ein Element, mehrere | Die drei Fälle, in denen Schleifen sich verrechnen |
| 5 | Geschachtelte Verzweigungen erst, wenn die äußere sitzt, eine Ebene je Schritt. `a && b` und `a \|\| b`: ein Test je Operand | So bleibt jeder Test ein Weg durch die Methode, nicht ein Bündel |
| 6 | Jede Ausnahme und jedes frühe `return` bekommt seinen Test | Die Abkürzungen sind die Wege, die keiner im Kopf hat |
| 7 | Nach jedem Test messen, nicht schätzen: Build, dann Abdeckung, wenn das Projekt ein Werkzeug dafür hat (JaCoCo-Bericht, ein Skript des Repos). Ohne Werkzeug die Landkarte abhaken: welche Seite welcher Verzweigung hat schon einen Test | Jede nicht erreichte Zeile ist ein fehlender Test; jede Kante ohne Paar auch |
| 8 | Volle Abdeckung ist nicht das Ende: Schicht 3 und 4 gelten auch für Zweige, die schon erreicht sind | Abdeckung sagt „erreicht“, nicht „geprüft“ |

## Regeln
| Nr | Regel | Warum |
|---|---|---|
| 1 | Ein Test, ein Weg durch die Methode, ein Verhalten | Bricht ein Test, sagt sein Name, was sich geändert hat |
| 2 | Name = beobachtetes Verhalten, in der Sprache der Tests des Projekts, nie `test1` | Die Tests sind die Doku des Legacy-Codes |
| 3 | Alles prüfen, was die Methode verändert, nicht nur den Rückgabewert: Parameter, Felder, Aufrufe an Abhängigkeiten | Was verändert wird und nicht geprüft ist, ist ungeschützt |
| 4 | Deterministisch: kein Zufall, keine Uhr, kein Netz. Braucht die Methode eine feste Abhängigkeit, wird sie an einer Naht ersetzt; gibt es keine Naht, Stopp und sagen, welche fehlt | Der Test muss morgen dasselbe sagen |
| 5 | Produktivcode bleibt unverändert, auch kein `private` wird für einen Test geöffnet | Erst das Netz, dann der Umbau |
| 6 | Fehler nicht reparieren, sondern festhalten: Ist-Wert plus `// Beobachtung:` | Ob die Regel geändert wird, entscheidet später jemand bewusst |
| 7 | Ein Test, dann der Build, dann der nächste Test; nie mehrere Tests auf einmal schreiben, nie mehr als ein roter Test gleichzeitig | Kleine Schritte, klare Ursache; wer vier Tests auf einmal schreibt, gräbt nicht, er schüttet |
| 8 | Tests in die vorhandene Testklasse des Gegenstands; gibt es keine, eine neue, nach dem Gegenstand benannt | Eine Klasse je Gegenstand, keine Parallelklassen |

## Beispiel
```java
int discountPercent(int totalInCents, boolean member, int items) {
    if (totalInCents <= 0) throw new IllegalArgumentException("total");
    int percent = 0;
    if (totalInCents >= 10000) percent = 5;
    if (member) percent += 3;
    if (items > 10 && !member) percent += 1;
    return Math.min(percent, 8);
}
```
Landkarte: Eingänge `totalInCents`, `member`, `items`; Ausgang Rückgabewert oder Ausnahme; Verzweigungen V1 `totalInCents <= 0`, V2 `totalInCents >= 10000`, V3 `member`, V4 `items > 10 && !member`, V5 Deckel 8.

| Schritt | Test | Eingang | Erwartung |
|---|---|---|---|
| geradeaus | `smallOrderOfANonMemberGetsNothing` | 5000, false, 1 | 0 |
| V1 | `zeroTotalIsRejected` | 0, false, 1 | Ausnahme |
| V2, Kante | `nineThousandNineHundredNinetyNineIsStillSmall` | 9999, false, 1 | 0 |
| V2, Kante | `tenThousandGetsFivePercent` | 10000, false, 1 | 5 |
| V3 | `aMemberGetsThreePercentOnTop` | 5000, true, 1 | 3 |
| V4, Kante | `elevenItemsAddOnePercentForNonMembers` | 5000, false, 11 | 1 |
| V4, Kante | `tenItemsAddNothing` | 5000, false, 10 | 0 |
| V4, Operand | `elevenItemsAddNothingForMembers` | 5000, true, 11 | 3 |
| V5 | `aMemberWithABigOrderIsCappedAtEight` | 10000, true, 11 | 8 |

Jeder Test unterscheidet sich vom Nachbarn in einer Sache. Der letzte Test ist der erste, der zwei Verzweigungen zugleich nimmt, und das erst, als jede einzeln saß.

## Fertig
- Jede Verzweigung der Landkarte hat auf beiden Seiten einen Test, jede Kante ihr Paar, jede Schleife ihre drei Fälle.
- Das Abdeckungswerkzeug nennt keine Zeile mehr, wenn es eines gibt.
- Der Build ist grün.
- Die Beobachtungen stehen als Liste in der Antwort.

## Was das nicht kann
Es sagt, was der Code für diese Eingänge tut, nicht, ob das richtig ist. Zwischen den Tests bleibt der Code ungeschützt; wer breit absichern will, legt einen Golden Master dazu.
