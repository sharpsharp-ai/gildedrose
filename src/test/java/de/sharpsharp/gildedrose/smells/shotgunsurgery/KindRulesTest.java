package de.sharpsharp.gildedrose.smells.shotgunsurgery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import de.sharpsharp.gildedrose.Item;

public class KindRulesTest {

    private final Item brie = new Item("Aged Brie", 5, 12);
    private final Item sulfuras = new Item("Sulfuras, Hand of Ragnaros", 0, 80);
    private final Item vest = new Item("+5 Dexterity Vest", 10, 20);

    @Test
    public void agedBrieIsPricedByQuality() {
        assertThat(PriceTag.centsFor(brie), is(120));
    }

    @Test
    public void sulfurasHasAFixedPrice() {
        assertThat(PriceTag.centsFor(sulfuras), is(8000));
    }

    @Test
    public void agedBrieGoesIntoTheFridge() {
        assertThat(ShelfPlanner.shelfFor(brie), is("Kühlregal"));
    }

    @Test
    public void anOrdinaryItemGoesOnTheShelf() {
        assertThat(ShelfPlanner.shelfFor(vest), is("Regal"));
    }

    @Test
    public void sulfurasCarriesALegendaryLabel() {
        assertThat(Label.textFor(sulfuras), is("legendär"));
    }

    @Test
    public void anOrdinaryItemHasNoLabel() {
        assertThat(Label.textFor(vest), is(""));
    }
}
