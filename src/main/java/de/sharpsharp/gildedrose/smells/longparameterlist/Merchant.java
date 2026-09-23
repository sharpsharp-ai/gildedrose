package de.sharpsharp.gildedrose.smells.longparameterlist;

public class Merchant {
    private final String name;
    private final String city;

    public Merchant(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}
