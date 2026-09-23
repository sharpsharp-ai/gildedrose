---
name: refactor-in-small-steps
description: Legacy-Code unter Test in kleinen, grünen Schritten umbauen, ein Refactoring aus dem Katalog je Commit; grün heißt Commit, rot heißt zurück, nie vorwärts reparieren
---
# Refactoring in kleinen Schritten

Ein Refactoring ändert die Struktur und lässt das Verhalten, wie es ist. Der Beweis ist das Netz: Nach jedem Schritt sind alle Tests grün. Ein Schritt, der das Netz reißt, war kein Refactoring, er wird zurückgenommen, nicht repariert.

| Nr | Regel | Warum |
|---|---|---|
| 1 | Erst das Netz. Vor dem ersten Schritt sind die Tests grün und decken den Code ab, der umgebaut wird. Kein Netz, kein Umbau | Ohne Netz ist jeder Schritt eine Wette |
| 2 | Ein Schritt ist ein Refactoring aus dem Katalog, mit Namen. Zwei Dinge sind zwei Schritte | Bricht das Netz, ist die Ursache eindeutig |
| 3 | Nach jedem Schritt das Skript des Repos: grün heißt Commit, rot heißt Produktivcode zurück auf den letzten Commit. Nie vorwärts reparieren; nach Rot kommt derselbe Schritt kleiner oder ein anderer | Reparieren unter Rot stapelt Vermutungen auf einen Fehler |
| 4 | Extrahieren kopiert wörtlich: Ausdruck, Block und Bedingung bleiben Zeichen für Zeichen, auch wenn sie hässlich sind. Eine Bedingung vereinfachen, negieren oder erweitern ist ein eigener Schritt, allein verifiziert | Die meisten gerissenen Netze kommen von einer „verbesserten" Bedingung beim Extrahieren |
| 5 | Verhalten bleibt, auch falsches. Ein Fehler im Code wird als Beobachtung notiert, nicht behoben | Ob ein Fehler ein Fehler ist, entscheidet später jemand bewusst |
| 6 | Tests bleiben unverändert, kein Golden Master wird während des Umbaus genehmigt. Wird ein Test rot, ist der Schritt falsch, nicht der Test | Das Netz gehört nicht der Hand, die umbaut |
| 7 | Klein ist, was das Skript nimmt. Zu groß heißt teilen, nicht argumentieren | Die Größe ist gemessen, nicht gefühlt |
| 8 | Commit-Botschaft = Name des Refactorings und Ziel: `Extract Method: Preis je Position` | Die Historie ist die Liste der Schritte |
| 9 | Reihenfolge: erst lesbar (Namen, lokale Variablen, Konstanten, Extract Method), dann Struktur (Bedingungen entwirren, Guard Clauses), zuletzt Design (Move, Polymorphie). Jede Stufe hinterlässt grünen Code | Design-Schritte auf unlesbarem Code raten |
| 10 | Ein Ziel je Auftrag, messbar: Methodenlänge, Verschachtelungstiefe, Punkte im Report. Fertig, wenn das Ziel steht oder der nächste Schritt das Verhalten ändern müsste | Ohne Ziel hört der Umbau nie auf |

## Katalog

| Refactoring | Mechanik | Typischer Fehler |
|---|---|---|
| Rename | ein Name, alle Verwendungen, sonst nichts | zwei Namen in einem Schritt |
| Extract Variable | ein wiederholter Ausdruck in eine lokale Variable, Ausdruck wörtlich | den Ausdruck beim Kopieren „verbessern" |
| Extract Method | ein Block in eine Methode, Rumpf wörtlich, Parameter sind die benutzten Variablen | die Bedingung um den Block herum mitverändern; zwei Blöcke auf einmal |
| Inline | Methode oder Variable an der Verwendungsstelle einsetzen | eine Methode mit Nebenwirkung an zwei Stellen einsetzen |
| Replace Magic Literal | eine Zahl oder ein Text wird eine benannte Konstante, ein Literal je Schritt | gleiche Literale mit verschiedener Bedeutung zusammenlegen |
| Decompose Conditional | Bedingung, Dann-Zweig, Sonst-Zweig je in eine Methode, wörtlich | die Bedingung beim Extrahieren negieren oder erweitern |
| Consolidate Conditional | zwei Bedingungen mit gleichem Ergebnis zu einer, eigener Schritt | mit Extract Method vermischen |
| Replace Nested Conditional with Guard Clauses | ein Sonderfall nach vorn mit `return` oder `continue`, je Schritt einer | die Reihenfolge der Prüfungen ändert das Ergebnis |
| Move Method | Methode dorthin, wo ihre Daten sind, Rumpf wörtlich | beim Umzug „aufräumen" |
| Replace Conditional with Polymorphism | viele Schritte: erst die Klassen, dann je Zweig einer | alles auf einmal |

## Beispiel, drei Schritte

Vorher, unter Test:

```java
public int shippingCost(Order order) {
    if (order.total() >= 10000) {
        if (order.isExpress()) { return 990; } else { return 0; }
    } else {
        if (order.isExpress()) { return 1490; } else { return 490; }
    }
}
```

| Schritt | Commit | Was sich ändert |
|---|---|---|
| Extract Variable | `Extract Variable: freeShipping` | `boolean freeShipping = order.total() >= 10000;` ersetzt den Ausdruck im `if`, wörtlich |
| Extract Method | `Extract Method: expressSurcharge` | die beiden `isExpress`-Zweige bleiben, wie sie sind; nur der Rumpf wandert |
| Guard Clause | `Guard Clause: express first` | `if (order.isExpress()) return freeShipping ? 990 : 1490;` nach vorn, Rest folgt |

Nach jedem Schritt das Skript. Hätte Schritt 1 die Schwelle „zufällig" auf `> 10000` geändert, wäre er rot und zurück, nicht repariert.

## Fertig
- Jeder Commit seit Start ist ein Refactoring mit Namen und war grün.
- Das Ziel ist erreicht oder benannt, warum nicht ohne Verhaltensänderung.
- Beobachtungen zum Verhalten stehen in der Antwort, nicht im Code.

## Was das nicht kann
Es macht falsches Verhalten nicht richtig und fehlende Tests nicht vorhanden. Es sagt nur: Der Code tut nach jedem Schritt, was er vorher tat.
