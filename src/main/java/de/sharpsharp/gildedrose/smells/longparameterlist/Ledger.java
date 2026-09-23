package de.sharpsharp.gildedrose.smells.longparameterlist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Smell: Long Parameter List. record hat acht Parameter, String und int in bunter Folge. Der Aufrufer muss die
 * Reihenfolge wissen, der Compiler hilft nicht: Menge und Stückpreis vertauscht ist ein gültiger Aufruf
 * (LedgerTest zeigt es). Die Liste wächst mit jeder Anforderung um einen Parameter, und das boolean am Ende
 * schaltet zwischen zwei Verhalten um.
 *
 * Woran man es erkennt: mehr als drei Parameter, Aufrufe über mehrere Zeilen, Aufrufer, die ein Objekt in
 * Einzelteile zerlegen, um es zu übergeben (merchant.getName(), merchant.getCity()). Der Clean-Code-Report
 * nennt das Kofferträger, das boolean Schalter (Flag Argument).
 *
 * Ziel: record(OrderLine line, Merchant merchant, Delivery delivery), bezahlt und offen als zwei Methoden
 * statt eines Flags. Drei Parameter mit Namen statt acht Werte in Reihenfolge.
 *
 * Weg in IntelliJ, Mac / Windows:
 * 1. Cursor auf record, Refactor This ⌃T / Ctrl+Alt+Shift+T, Introduce Parameter Object: itemName, quantity und
 *    unitPriceInCents auswählen, Klasse OrderLine. IntelliJ schreibt jeden Aufrufer um, auch den Test. Deshalb
 *    Signaturen nur mit dem Werkzeug ändern, nie von Hand. Dasselbe für orderedOn und deliveredOn als Delivery.
 * 2. Preserve Whole Object: Change Signature ⌘F6 / Ctrl+F6, Parameter Merchant merchant hinzufügen, Default im
 *    Aufruf merchant. Im Rumpf merchant.getName() und merchant.getCity() verwenden, dann supplierName und
 *    supplierCity mit Change Signature entfernen.
 * 3. Cursor auf OrderLine, ⌥⏎ / Alt+Enter, Convert to record; die Rechnung quantity mal unitPriceInCents zieht
 *    als totalInCents() mit um (Extract Method ⌥⌘M / Ctrl+Alt+M, dann F6). Ebenso Delivery.
 * 4. Das Flag: den Textaufbau markieren, Extract Method ⌥⌘M / Ctrl+Alt+M entry. Dann zwei öffentliche Methoden
 *    recordPaid und recordOpen, die entry nutzen; record verschwindet per Safe Delete, sobald die Aufrufer
 *    umgestellt sind. Die wechseln von Hand, es sind wenige.
 * 5. Wächst ein Konstruktor so: Refactor | Replace Constructor with Builder.
 * 6. Was bleibt: quantity und unitPriceInCents sind in OrderLine noch zwei int nebeneinander. Wer auch das
 *    absichern will, gibt dem Preis einen Typ. Das ist Primitive Obsession, das Nachbarbeispiel.
 */
public class Ledger {
    private final List<String> lines = new ArrayList<>();
    private int openInCents;

    public void record(String itemName, int quantity, int unitPriceInCents, String supplierName, String supplierCity,
                       LocalDate orderedOn, LocalDate deliveredOn, boolean paid) {
        int total = quantity * unitPriceInCents;
        lines.add(quantity + " x " + itemName + " von " + supplierName + ", " + supplierCity
                + ", bestellt " + orderedOn + ", geliefert " + deliveredOn + ": " + total + " Cent"
                + (paid ? "" : ", offen"));
        if (!paid) {
            openInCents += total;
        }
    }

    public List<String> lines() {
        return List.copyOf(lines);
    }

    public int openInCents() {
        return openInCents;
    }
}
