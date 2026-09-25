---
name: approval-test
description: Golden Master mit ApprovalTests über eine Methode, das Raster aus den Literalen der Methode abgeleitet, jede Kante mit ihren Nachbarn, als schnelles Netz vor dem Umbau von Legacy-Code
---
# Approval-Test als Golden Master über eine Methode

Ein Golden Master friert die Ausgabe einer Methode über viele Eingaben in einer Datei ein. Er versteht nichts und merkt alles: Jede Verhaltensänderung ist ein Diff. Das Netz steht in Minuten. Das Verfahren gilt für jede Methode in jeder Sprache; das Muster unten ist Java mit ApprovalTests.

## Vorher: die Methode
Ist keine Methode genannt (`Klasse#methode`), nenne die Kandidaten, die du im Produktivcode siehst, schlage den wahrscheinlichsten vor (die öffentliche Methode mit den meisten Verzweigungen) und warte auf die Antwort. Ist nur eine Klasse genannt, gilt dasselbe für ihre Methoden. Keine Datei entsteht, bevor die Methode feststeht.

## Die Landkarte
Lies die Methode ganz, samt der privaten Methoden, die sie ruft, und schreibe in die Antwort, bevor der erste Test entsteht:
- Eingänge: Parameter; Felder, die die Methode liest, und die Konstruktor- oder Fabrik-Parameter, über die sie gesetzt werden; Abhängigkeiten.
- Ausgänge: Rückgabewert, veränderte Parameter, veränderte Felder, Ausnahmen.
- Die Wertetabelle: je Eingang eine Zeile mit den Literalen, gegen die die Methode ihn vergleicht (mit Zeilennummer), und den Werten, die nach der Tabelle unten daraus folgen.

## Werte je Eingang, hergeleitet, nicht geraten
| Eingang | Werte | Warum |
|---|---|---|
| Zahl | je Literal n, gegen das die Methode den Eingang vergleicht: n-1, n, n+1; dazu ein Wert weit jenseits des größten Literals | Legacy-Code lebt von `<` und `<=`; ein Wert in der Mitte sieht die Kante nicht |
| Text, den die Methode vergleicht (`equals`, `switch`) | jedes Literal wörtlich; dazu ein Fremdwort, das in der Methode nicht vorkommt | Die Literale sind die Verzweigungen, das Fremdwort ist der Normalfall |
| Text, den die Methode zerlegt (`startsWith`, `charAt`, `split`, `indexOf`) | je Literal ein Text, der es an der geprüften Stelle enthält, und einer ohne; der leere Text; Zahlen im Text nach der Zahlenregel | Die Kante sitzt im Aufbau des Textes, nicht in seinem Wert |
| Wahrheitswert | beide | |
| Sammlung | leer, ein Element, mehrere; die Elemente aus den Werten der anderen Eingänge | Die drei Fälle, in denen Schleifen sich verrechnen |
| `null` | nur, wenn die Methode darauf prüft | Sonst friert die Datei einen Absturz ein, den niemand braucht |

Konstanten der Klasse zählen wie Literale. Ein Literal, das nur gegen einen Zwischenwert steht (ein Deckel auf dem Ergebnis), liefert keinen Eingabewert; das Raster erreicht es über die Kombinationen.

## Regeln
| Nr | Regel | Warum |
|---|---|---|
| 1 | Das Raster ist das Kreuzprodukt der Wertelisten, in fester Reihenfolge, ohne Zufall | Zufall ohne Seed flattert; im Raster ist jede Kante sicher drin |
| 2 | Ein frisches Objekt je Rasterpunkt, ein Aufruf | Rasterpunkte beeinflussen sich nicht; bricht eine Zeile, zeigt sie die Ursache |
| 3 | Deckel etwa 1000 Zeilen: erst die Fernwerte streichen, dann Fremdwörter; die Nachbarn der Kanten bleiben | Die Datei muss lesbar bleiben, und die Kanten sind ihr Wert |
| 4 | Deterministisch: keine Uhr, kein Zufall, kein Netz. Eine Abhängigkeit wird an einer Naht durch feste Werte ersetzt; gibt es keine Naht, Stopp und sagen, welche fehlt | Der Test muss morgen dasselbe sagen |
| 5 | Die Zeile formt der Test selbst: Eingaben, Pfeil, alle Ausgänge. Rückgabewert; bei einer Methode ohne Rückgabe der Zustand danach, jedes veränderte Feld über seinen Getter, nie über `toString` einer Produktivklasse; Ausnahme als Klasse und Meldung | `toString` des Produktivcodes darf sich beim Umbau ändern; eine Formatänderung sähe sonst aus wie eine Verhaltensänderung |
| 6 | Vor dem Genehmigen lesen: je Kante die Zeile diesseits und jenseits gegen den Code halten. Dann `received` zu `approved`, mit dem Skript des Repos, sonst von Hand | Genehmigen heißt „so ist es", nicht „so soll es sein" |
| 7 | Erneut genehmigen nur, wenn das Raster sich geändert hat, nie nach einer Änderung am Produktivcode | Sonst genehmigt man den Fehler mit |
| 8 | `*.approved.txt` wird eingecheckt, `*.received.txt` nie | Die genehmigte Datei ist der Test |
| 9 | Produktivcode bleibt unverändert, auch kein `private` wird für einen Test geöffnet | Erst das Netz, dann der Umbau |
| 10 | Danach messen, wenn das Projekt ein Abdeckungswerkzeug hat: eine nicht erreichte Zeile in der Methode oder in einer Methode, die sie ruft, heißt, das Literal darin fehlt im Raster. Nachtragen, neu genehmigen. Zeilen außerhalb bleiben unerreicht, sie gehören nicht zu diesem Netz | Abdeckung sagt, welche Kante das Raster verfehlt; toter Code ist ein Fund, kein Testziel |

## Muster

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

Wertetabelle:

| Eingang | Literale (Zeile) | Werte |
|---|---|---|
| `totalInCents` | 0 (2), 10000 (4) | -1, 0, 1, 9999, 10000, 10001, 1_000_000 |
| `member` | (5) | false, true |
| `items` | 10 (6) | 9, 10, 11, 1000 |

Die 8 in Zeile 7 deckelt das Ergebnis, kein Eingabewert. 7 × 2 × 4 = 56 Zeilen.

```java
package de.example.shop;

import java.util.ArrayList;
import java.util.List;

import org.approvaltests.Approvals;
import org.junit.Test;

public class PricingGoldenMasterTest {

    // Literale in discountPercent: totalInCents <= 0, totalInCents >= 10000, items > 10.
    // Die 8 deckelt das Ergebnis, sie ist kein Eingang.
    private static final int[] TOTAL_IN_CENTS = {-1, 0, 1, 9999, 10000, 10001, 1_000_000};
    private static final boolean[] MEMBER = {false, true};
    private static final int[] ITEMS = {9, 10, 11, 1000};

    record Case(int totalInCents, boolean member, int items) {}

    @Test
    public void discountPercent() {
        List<Case> cases = new ArrayList<>();
        for (int total : TOTAL_IN_CENTS) {
            for (boolean member : MEMBER) {
                for (int items : ITEMS) {
                    cases.add(new Case(total, member, items));
                }
            }
        }
        Approvals.verifyAll("discountPercent", cases, c -> c + " -> " + result(c));
    }

    private static String result(Case c) {
        try {
            return String.valueOf(new Pricing().discountPercent(c.totalInCents(), c.member(), c.items()));
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName() + ": " + e.getMessage();
        }
    }
}
```

`Approvals.verifyAll` schreibt `PricingGoldenMasterTest.discountPercent.received.txt` neben die Klasse, eine Zeile je Rasterpunkt:
`Case[totalInCents=-1, member=false, items=9] -> IllegalArgumentException: total`. Genehmigt heißt sie `.approved.txt`.

Bei einer Methode ohne Rückgabewert baut `result` das Objekt aus dem Rasterpunkt, ruft die Methode und schreibt die veränderten Felder über ihre Getter in die Zeile, nicht über `toString` des Objekts.

## Fertig
- Die Landkarte mit der Wertetabelle steht in der Antwort, jeder Wert hat sein Literal.
- Der Build ist grün, die `.approved.txt` liegt neben dem Test.
- Das Abdeckungswerkzeug nennt keine Zeile mehr, wenn es eines gibt.
- In der Antwort: Zeilenzahl der Datei, die Stichproben je Kante, ein Satz, was dieses Netz nicht sagen kann.

## Was der Golden Master nicht kann
Er sagt nicht, welche Regel gebrochen ist, und er dokumentiert keine. Dafür gibt es `/characterization-test`. Zusammen: erst das grobe Netz, dann die feinen Tests, dann der Umbau.
