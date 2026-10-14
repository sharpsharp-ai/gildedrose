package de.sharpsharp.gildedrose;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.out.println("OMGHAI!");
        GildedRose app = GildedRose.with(
        		new Item("+5 Dexterity Vest", 10, 20),
        		new Item("Aged Brie", 2, 0),
        		new Item("Elixir of the Mongoose", 5, 7),
        		new Item("Sulfuras, Hand of Ragnaros", 0, 80),
        		new Item("Backstage passes to a TAFKAL80ETC concert", 15,20),
        		new Item("Conjured Mana Cake", 3, 6)
        		);
        dump(System.out, "Before update", app.items);
        app.updateQuality();
        dump(System.out, "After update", app.items);
    }

    public static void dump(PrintStream out, String text, List<Item> items) {
    	out.println(text + ":\n" + items.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }
}
