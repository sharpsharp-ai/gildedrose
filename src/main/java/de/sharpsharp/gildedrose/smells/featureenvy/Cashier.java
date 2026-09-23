package de.sharpsharp.gildedrose.smells.featureenvy;

/**
 * Smell: Feature Envy. Cashier.amountFor rechnet nur mit Daten von Stay: Nächte, Zimmerpreis, Frühstücke, und
 * nutzt nichts Eigenes. Die Methode beneidet Stay um seine Felder, sie wohnt in der falschen Klasse. Wer die
 * Preisregel ändern will, sucht sie beim Kassierer statt beim Aufenthalt, und die nächste Klasse, die einen
 * Betrag braucht, schreibt die Rechnung noch einmal ab.
 *
 * Woran man es erkennt: eine Methode ruft drei oder mehr Getter eines fremden Objekts und kaum eigene Felder.
 * Der Clean-Code-Report nennt das Neider und zählt die Zugriffe. Stay ist im selben Moment ein Karteikasten:
 * nur Daten, keine Regeln.
 *
 * Ziel: Stay.amount() kennt seine eigene Rechnung, Cashier.collect(stay) legt nur noch stay.amount() in die
 * Schublade. Was der Kasse bleibt, ist ihre Schublade.
 *
 * Weg in IntelliJ, Mac / Windows:
 * 1. Cursor auf amountFor, F6: Move Instance Method, Ziel ist der Parameter stay. IntelliJ zieht die Aufrufer
 *    nach, aus amountFor(stay) wird stay.amountFor(). Rename ⇧F6 / Shift+F6 nach amount.
 * 2. Die Zahlen: Extract Constant ⌥⌘C / Ctrl+Alt+C auf 1200 (BREAKFAST_IN_CENTS) und 7 (NIGHTS_FOR_DISCOUNT).
 * 3. Bleibt ein Teil, der nur das Zimmer betrifft: Extract Method ⌥⌘M / Ctrl+Alt+M roomCharge; gehört das Zimmer
 *    später einer eigenen Klasse, zieht die Methode mit F6 dorthin.
 * 4. Vorsicht beim Ziel: Item aus der Kata gehört dem Goblin. Beneidet eine Methode eine Klasse, die nicht
 *    geändert werden darf, hilft Introduce Local Extension: eine eigene Klasse um Item herum, die die Methode
 *    aufnimmt. Sonst wandert die Regel in eine Klasse, die ihr niemand abnimmt.
 */
public class Cashier {
    private int drawerInCents;

    public int amountFor(Stay stay) {
        int rooms = stay.getNights() * stay.getRateInCents();
        if (stay.getNights() >= 7) {
            rooms = rooms * 9 / 10;
        }
        int breakfasts = stay.getBreakfasts() * 1200;
        return rooms + breakfasts;
    }

    public void collect(Stay stay) {
        drawerInCents += amountFor(stay);
    }

    public int drawerInCents() {
        return drawerInCents;
    }
}
