package de.sharpsharp.gildedrose.smells.shotgunsurgery;

import de.sharpsharp.gildedrose.Item;

public class ShelfPlanner {

    public static String shelfFor(Item item) {
        return switch (item.getName()) {
            case "Aged Brie" -> "Kühlregal";
            case "Sulfuras, Hand of Ragnaros" -> "Vitrine";
            case "Backstage passes to a TAFKAL80ETC concert" -> "Kasse";
            default -> "Regal";
        };
    }
}
