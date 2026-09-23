package de.sharpsharp.gildedrose.smells.inappropriateintimacy;

import java.util.ArrayList;
import java.util.List;

import de.sharpsharp.gildedrose.Item;

/**
 * Smell: Inappropriate Intimacy. Shelf und Stockroom wühlen in den Innereien des jeweils anderen: Stockroom
 * füllt das Regal, indem es in shelf.getItems() schreibt und die Kapazität selbst vergleicht; Shelf holt sich
 * nach einem Verkauf Ersatz direkt aus stockroom.getReserve(). Jede Klasse hängt an der Datenstruktur der
 * anderen, und die Regel „ein Regal ist voll, wenn“ steht nicht im Regal.
 *
 * Woran man es erkennt: Getter, die eine veränderbare Liste herausgeben, und Aufrufer, die darauf add, remove
 * oder size rufen. Find Usages ⌥F7 / Alt+F7 auf getItems zeigt, wer hineingreift. Verweise in beide
 * Richtungen (Shelf kennt Stockroom, Stockroom bearbeitet Shelf) sind das zweite Zeichen. Der Clean-Code-Report
 * misst diesen Smell nicht; er zeigt Stockroom.restock als Neider, weil es dreimal in shelf greift.
 *
 * Ziel: jede Klasse verwaltet ihre Liste selbst. Shelf bekommt put(item), hasRoom() und ein take(name), das
 * nur verkauft; Stockroom bekommt takeOne() und takeByName(name). Stockroom.restock spricht nur noch über diese
 * Methoden, und der Ersatz nach dem Verkauf läuft über dieselbe schmale Tür.
 *
 * Weg in IntelliJ, Mac / Windows:
 * 1. In Stockroom.restock: Extract Variable ⌥⌘V / Ctrl+Alt+V auf reserve.remove(0), Name item. Dann die Zeile
 *    shelf.getItems().add(item) markieren, Extract Method ⌥⌘M / Ctrl+Alt+M, Name put. F6 darauf: Move Instance
 *    Method, Ziel ist der Parameter shelf. Ebenso die Kapazitätsprüfung als hasRoom.
 * 2. In Shelf.take die Schleife über stockroom.getReserve() so umbauen, dass sie nur sucht und entfernt und den
 *    Fund in eine Variable legt. Dann Extract Method takeByName und F6 nach Stockroom, Ziel ist das Feld stockroom.
 * 3. Refactor | Encapsulate Fields auf items und reserve: die Getter geben ab jetzt List.copyOf zurück.
 *    Schreibzugriffe von außen fallen im Test auf, nicht erst im Laden. Safe Delete ⌘⌫ / Alt+Delete auf einen
 *    Getter zeigt in der Vorschau, wer noch hineingreift.
 * 4. Nur eine Richtung: Shelf kennt sein Lager, das Lager kennt kein Regal, es bekommt eines gereicht.
 *    Alles, was Stockroom über Shelf weiß, sind put und hasRoom.
 * 5. mvn -q verify nach jedem Schritt; die Tests lesen weiter über getItems und getReserve.
 */
public class Shelf {
    private final List<Item> items = new ArrayList<>();
    private final int capacity;
    private Stockroom stockroom;

    public Shelf(int capacity) {
        this.capacity = capacity;
    }

    public void setStockroom(Stockroom stockroom) {
        this.stockroom = stockroom;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getCapacity() {
        return capacity;
    }

    public Item take(String name) {
        Item sold = items.stream().filter(item -> item.getName().equals(name)).findFirst().orElseThrow();
        items.remove(sold);
        for (Item spare : stockroom.getReserve()) {
            if (spare.getName().equals(name)) {
                stockroom.getReserve().remove(spare);
                items.add(spare);
                break;
            }
        }
        return sold;
    }
}
