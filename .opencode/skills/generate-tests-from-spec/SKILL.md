---
name: generate-tests-from-spec
description: Tests aus dem Anforderungsdokument ableiten, je Satz Normalfall, Schwellen, Ränder, Kollisionen und Offene Fragen; der Code bleibt zu, rot ist ein Fund
---
# Spec-Tests aus dem Anforderungsdokument

Ein Spec-Test hält fest, was der Code laut Anforderung tun soll. Er entsteht aus dem Dokument, nicht aus dem Code. Wer den Code liest, schreibt Characterization Tests unter anderem Namen. Rot ist hier ein Fund: Der Code weicht ab, oder die Regel ist noch nicht gebaut.

| Nr | Regel | Warum |
|---|---|---|
| 1 | Der Code bleibt zu. Gelesen werden das Dokument und die Schnittstelle: Konstruktoren, öffentliche Methoden, Getter. Nie der Rumpf, auch nicht bei Rot | Sonst wandert das Verhalten des Codes in die Erwartung, und der Test prüft den Code gegen sich selbst |
| 2 | Erst der Regelkatalog: die Sätze des Dokuments nummeriert, R1 bis Rn, mit den Zahlen und Signalwörtern je Satz (das Skript des Repos macht das und prüft später je Zahl, ob die Tests auch den Nachbarn als Eingabe benutzen). Verhalten im Fließtext bekommt die Nummer nach der letzten der Liste, auch wenn Nummern der Liste außerhalb des Bereichs liegen. Jeder Test nennt seine Regel in einer Kommentarzeile, die mit der Nummer beginnt: `// R2: "<Zitat>"`, bei zwei Regeln `// R2, R5: ...` | Jeder Satz hat seine Tests, und das Skript nennt Sätze ohne Test |
| 3 | Vor dem ersten Test die Ableitungstabelle: eine Zeile je Regel, Spalten Schwellen, Ränder, Kollisionen, Offen. Jede Zelle ist gefüllt oder sagt „keine" mit Grund | Was nicht in der Tabelle steht, wird nicht getestet; die Tabelle macht die Lücke sichtbar, bevor der Code sie versteckt |
| 4 | Schwellen: je Zahl im Satz zwei Tests, auf der Schwelle und daneben. „100 € or more" heißt 100 und 99,99. „from 3 items" heißt 3 und 2. „after the deadline" heißt der letzte Tag und der Tag danach | Fehler wohnen bei `<` und `<=` |
| 5 | Ränder: je „never", „always", „every", „each" ein Test am Rand. „never negative": der Wert 0 und 1 vor dem Abzug. „never more than X": X-1 und X vor dem Zuwachs. „every item": mehrere Elemente in einem Aufruf, jedes geprüft. Ein Zähler, der weiterläuft: der Wert am Ende und der dahinter | Ein Verbot ohne Test am Rand ist ein Wunsch |
| 6 | Kollisionen: je Paar Regeln, die sich treffen können, ein Test. Deckel gegen Zuwachs, Untergrenze gegen doppelten Abzug, jede Ausnahme gegen die allgemeine Regel, und zwar an jedem Rand, den die allgemeinen Regeln kennen: was dort 0 und -1, X-1 und X prüft, prüft auch die Ausnahme | Die Fehler sitzen zwischen den Regeln; eine Ausnahme, die nur im Normalfall geprüft ist, bricht an den Rändern der Regel, die sie aufhebt |
| 7 | Erwartung nur aus dem Dokument. Nennt der Satz die Zahl („10 % off"), prüft der Test sie: `is(3_00)`. Nennt er keine, aber ein anderer Satz über dieselbe Sache und dieselbe Richtung legt sie fest, steht `// Annahme:` mit dem Zitat über dem Test. Ein Satz über das Sinken sagt nichts über das Steigen. Legt kein Satz sie fest, prüft der Test nur die Richtung, `greaterThan(before)`, und die Zahl steht unter Offen | Ein geratener Wert schreibt die Vermutung des Autors fest; eine Annahme mit Zitat kann jemand prüfen |
| 8 | Offen ist jede Stelle, an der zwei Leser verschiedene Tests schreiben würden: ein relatives Wort ohne Bezug („twice as fast": ausgehend wovon?), ein Satz ohne Zahl, ein Bereich ohne Rand (was gilt unter 0, was über dem Deckel beim Anlegen?). „Keine" ist bei Prosa fast nie richtig | Fragen an den Kunden statt Antworten aus dem Kopf |
| 9 | Rot bleibt rot. Vorher zwei Prüfungen: Liest der Test den Satz richtig? Ist der Satz eindeutig? Beides ja: der Code weicht ab, und das steht in der Antwort | Der Erwartungswert wird nie an den Code angepasst; sonst ist es kein Spec-Test mehr |
| 10 | Name = Regel in Worten: `discountIsNeverMoreThanThirtyEuro`, `threeItemsGetTenPercentOff`. Eine Klasse je Gegenstand des Dokuments (je Warenart, je Tarif, je Rolle), Suffix `SpecTest`. Ein Objekt, eine Aktion, und alle Werte prüfen, die die Regel berührt, auch die, die gleich bleiben sollen | Bricht der Test, sagt sein Name, welche Regel; Nebenwirkungen fallen sonst durch |
| 11 | Namen und Texte wörtlich, so wie das System sie kennt: aus dem Dokument oder aus den Konstanten der Schnittstelle, nie erfunden, nie gekürzt; deterministisch, keine Mocks, kein Zufall, keine Uhr | Der Test muss morgen dasselbe sagen |
| 12 | Produktivcode bleibt unverändert, auch wenn ein Test rot ist | Erst das Netz, dann der Umbau |

## Ableitungstabelle, an einem Satz gezeigt

> Orders of 100 € or more ship free. From 3 items the customer gets 10 % off, from 10 items 20 %, but never more than 30 € off.

Das Skript sagt dazu: Zahlen 100, 3, 10, 10, 20, 30; Wörter more, never. Daraus die Zeilen der Tabelle:

| Regel | Schwellen (auf / daneben) | Ränder | Kollisionen | Offen |
|---|---|---|---|---|
| R1 Versand frei ab 100 € | 100,00 / 99,99 | keine, kein Signalwort | R2: zählt der Betrag vor oder nach dem Rabatt? | genau das: vor oder nach dem Rabatt |
| R2 Rabattstaffel, Deckel 30 € | 3 / 2, 10 / 9 | „never more than 30 €": Rabatt 30,00 und 30,01 vor dem Deckel | R1 wie oben | gilt der Deckel je Bestellung oder je Position? |

10 und 20 sind Beträge, keine Schwellen. Fünf Schwellen-Tests, zwei Rand-Tests, eine Kollision, zwei Offene Fragen.

## Muster

```java
// R2: "from 3 items the customer gets 10 % off", auf der Schwelle
@Test
public void threeItemsGetTenPercentOff() {
    Order order = Order.of(3, 10_00);
    order.checkout();
    assertThat(order.discount(), is(3_00));
    assertThat(order.total(), is(27_00));
}

// R2: "from 3 items the customer gets 10 % off", daneben: 2 Positionen sind nicht "from 3 items"
@Test
public void twoItemsGetNoDiscount() {
    Order order = Order.of(2, 10_00);
    order.checkout();
    assertThat(order.discount(), is(0));
    assertThat(order.total(), is(20_00));
}

// R2: "never more than 30 € off", auf dem Rand
// Annahme: 20 % gelten ab 10 Positionen, weil "from 10 items 20 %" im selben Satz steht
@Test
public void discountIsNeverMoreThanThirtyEuro() {
    Order order = Order.of(10, 20_00);
    order.checkout();
    assertThat(order.discount(), is(30_00));
    assertThat(order.total(), is(170_00));
}
```

Schnittstelle, Zitat, Erwartung. Kein Blick in `Order.checkout()`.

## Checkliste vor dem Abgeben, je Regel ja oder nein
1. Jede Zahl im Satz hat zwei Tests, auf der Schwelle und daneben? (4)
2. Jedes Signalwort hat seinen Test am Rand? (5)
3. Jede Regel, die diese treffen kann, hat einen Kollisionstest? (6)
4. Jede Zahl in einer Erwartung steht im Satz oder in einer Annahme mit Zitat? (7)
5. Zwei Leser würden denselben Test schreiben? Bei nein: Offene Frage (8)
6. Kein Erwartungswert und kein Name kommt aus dem Rumpf oder aus dem Kopf? (1, 9, 11)

Ein nein bei 1 bis 4: Test nachtragen. Ein nein bei 5: Offene Frage in die Antwort. Ein nein bei 6: Test neu aus dem Satz.

## Fertig
- Die Ableitungstabelle steht, jede Zelle hat Tests oder „keine" mit Grund.
- Die Checkliste je Regel ist durch, jedes nein ist behoben oder als Offene Frage notiert.
- Das Skript nennt im Bereich keine Regel ohne Test und keine Schwelle ohne Nachbarn.
- Jeder rote Test steht in der Antwort mit Regel und Ursache: Abweichung oder noch nicht gebaut.
- Annahmen mit Zitat, Offene Fragen als Liste.

## Was der Spec-Test nicht kann
Er schützt nur, was im Dokument steht. Verhalten, das der Code darüber hinaus hat, sieht er nicht; dafür Characterization Tests und Approval-Tests. Zusammen: Der Spec-Test sagt, was fehlt oder abweicht, die beiden anderen sagen, was sich beim Umbau ändert.
