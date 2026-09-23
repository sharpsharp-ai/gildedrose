package de.sharpsharp.gildedrose.smells.primitiveobsession;

public class Postage {

    public static int centsFor(Supplier supplier, int grams) {
        boolean domestic = "AT".equals(supplier.getCountry());
        int base = domestic ? 300 : 900;
        int perKilo = domestic ? 150 : 400;
        return base + perKilo * (grams / 1000);
    }
}
