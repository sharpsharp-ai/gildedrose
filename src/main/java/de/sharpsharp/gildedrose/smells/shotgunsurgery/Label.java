package de.sharpsharp.gildedrose.smells.shotgunsurgery;

import de.sharpsharp.gildedrose.Item;

public class Label {

    public static String textFor(Item item) {
        return switch (item.getName()) {
            case "Aged Brie" -> "reift nach";
            case "Sulfuras, Hand of Ragnaros" -> "legendär";
            case "Backstage passes to a TAFKAL80ETC concert" -> "nur bis zum Konzert";
            default -> "";
        };
    }
}
