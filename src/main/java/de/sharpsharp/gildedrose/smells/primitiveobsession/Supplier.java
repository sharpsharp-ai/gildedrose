package de.sharpsharp.gildedrose.smells.primitiveobsession;

/**
 * Smell: Primitive Obsession. Straße, Hausnummer, PLZ, Ort und Land sind fünf Strings, die immer zusammen
 * unterwegs sind, aber kein Objekt bilden. Was eine Adresse kann, steht deshalb dort, wo es gerade gebraucht
 * wird: die Anschrift in ShippingLabel, die Frage nach dem Inland in Postage. Nichts davon ist typgeprüft,
 * "AT", "Austria" und "Österreich" sind für den Compiler dasselbe.
 *
 * Woran man es erkennt: dieselben drei bis fünf Werte tauchen als Felder oder Parameter in mehreren Klassen
 * auf, und Regeln über sie stehen bei den Nutzern statt bei den Werten. Der Clean-Code-Report nennt die
 * Begleiterscheinungen: Kofferträger (Long Parameter List) am Konstruktor, Karteikasten (Data Class) für
 * Supplier und Neider für ShippingLabel, das siebenmal in Supplier greift.
 *
 * Ziel: ein Wertobjekt Address mit lines() und isDomestic(). Supplier hat eine Adresse statt fünf Strings,
 * und alles, was über Adressen zu wissen ist, wohnt in Address.
 *
 * Weg in IntelliJ, Mac / Windows:
 * 1. Cursor in Supplier, Refactor This ⌃T / Ctrl+Alt+Shift+T, Extract Delegate: die fünf Adressfelder
 *    auswählen, Klassenname Address, Getter erzeugen lassen. Supplier bekommt ein Feld address, der
 *    Konstruktor bleibt, die Tests laufen weiter.
 * 2. In ShippingLabel die drei Adresszeilen markieren, Extract Method ⌥⌘M / Ctrl+Alt+M, Name addressLines.
 *    Change Signature ⌘F6 / Ctrl+F6: den Parameter Supplier durch Address ersetzen, Wert supplier.getAddress().
 * 3. F6 auf addressLines, Ziel Address. Dann Refactor | Convert To Instance Method, damit aus
 *    addressLines(address) ein address.lines() wird. Rename ⇧F6 / Shift+F6 nach lines.
 * 4. Dasselbe mit der Inlandsprüfung in Postage: Extract Method isDomestic, F6 nach Address,
 *    Convert To Instance Method.
 * 5. Cursor auf den Klassennamen Address, ⌥⏎ / Alt+Enter, Convert to record. Nach jedem Schritt mvn -q verify.
 * 6. In derselben Richtung weiter: Country als enum, dann gibt es "Austria" nicht mehr. Und die fünf Getter
 *    in Supplier per Safe Delete ⌘⌫ / Alt+Delete entfernen, sobald niemand mehr einzeln an die Teile will.
 */
public class Supplier {
    private final String name;
    private final String street;
    private final String houseNumber;
    private final String zip;
    private final String city;
    private final String country;

    public Supplier(String name, String street, String houseNumber, String zip, String city, String country) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
