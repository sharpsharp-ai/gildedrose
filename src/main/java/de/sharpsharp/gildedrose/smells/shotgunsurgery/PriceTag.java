package de.sharpsharp.gildedrose.smells.shotgunsurgery;

import de.sharpsharp.gildedrose.Item;

/**
 * Smell: Shotgun Surgery. Was eine Warenart ausmacht, ist auf drei Klassen verteilt: PriceTag kennt ihren Preis,
 * ShelfPlanner ihren Platz, Label ihren Aufdruck, jede mit einem eigenen switch über den Namen. Kommt
 * „Conjured Mana Cake“ dazu, sind drei Klassen zu ändern; vergisst man eine, fällt es erst im Laden auf, weil
 * der default-Zweig den Fehler schluckt. Das Gegenstück heißt Divergent Change: GildedRose.updateQuality ändert
 * sich an einer Stelle, dafür aus jedem Grund.
 *
 * Woran man es erkennt: Find Usages ⌥F7 / Alt+F7 auf "Aged Brie" trifft mehrere Klassen, und dieselbe
 * Fallunterscheidung wiederholt sich. Der Clean-Code-Report zeigt den Weichensteller je Klasse und einmal extra,
 * weil derselbe switch in drei Methoden steht; Shotgun Surgery selbst misst er nicht, dafür bräuchte er die
 * Änderungshistorie.
 *
 * Ziel: eine Stelle je Warenart. Ein enum Kind mit AGED_BRIE, SULFURAS, BACKSTAGE_PASS und ORDINARY trägt
 * Regal und Aufdruck als Felder und die Preisregel als Methode; Kind.of(item) übersetzt den Namen. Eine neue
 * Art ist eine neue Zeile im enum, und der Compiler meldet jeden switch, der sie nicht kennt.
 *
 * Weg in IntelliJ, Mac / Windows:
 * 1. Die Namen einsammeln: Extract Constant ⌥⌘C / Ctrl+Alt+C auf "Aged Brie" mit Replace all occurrences,
 *    dann die anderen beiden. Das enum Kind mit einer statischen of(Item) von Hand anlegen, dafür gibt es kein
 *    Refactoring.
 * 2. Change Signature ⌘F6 / Ctrl+F6 auf centsFor: Parameter Kind kind hinzufügen, Default Kind.of(item). Im Rumpf
 *    switch (kind) statt switch über den Namen; die Fälle schreibt ⌥⏎ / Alt+Enter „Create missing branches“.
 *    Wer die Qualität noch braucht, übergibt item.getQuality() als int statt des ganzen Item.
 * 3. F6 auf centsFor, Move Members nach Kind, dann Refactor | Convert To Instance Method: aus
 *    centsFor(kind, quality) wird kind.centsFor(quality). Dasselbe mit shelfFor und textFor.
 * 4. Drei switches in einer Klasse nebeneinander: jetzt je Konstante ein Feld für Regal und Aufdruck (Konstruktor
 *    des enum) und die Preisregel als Methode je Konstante oder als Faktor im Konstruktor.
 * 5. PriceTag, ShelfPlanner und Label bleiben als Fassaden mit einer Zeile oder werden Inline ⌥⌘N / Ctrl+Alt+N.
 */
public class PriceTag {

    public static int centsFor(Item item) {
        return switch (item.getName()) {
            case "Aged Brie" -> item.getQuality() * 10;
            case "Sulfuras, Hand of Ragnaros" -> 8000;
            case "Backstage passes to a TAFKAL80ETC concert" -> item.getQuality() * 20;
            default -> item.getQuality() * 5;
        };
    }
}
