package de.sharpsharp.gildedrose.smells.inappropriateintimacy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import de.sharpsharp.gildedrose.Item;

public class RestockingTest {

    private final Shelf shelf = new Shelf(2);
    private final Stockroom stockroom = new Stockroom();

    @Before
    public void aShelfForTwoAndThreeItemsInTheStockroom() {
        shelf.setStockroom(stockroom);
        stockroom.deliver(new Item("Aged Brie", 5, 10));
        stockroom.deliver(new Item("Elixir of the Mongoose", 3, 7));
        stockroom.deliver(new Item("Aged Brie", 4, 9));
    }

    @Test
    public void restockingFillsTheShelfUpToItsCapacity() {
        stockroom.restock(shelf);
        assertThat(shelf.getItems(), hasSize(2));
    }

    @Test
    public void restockingLeavesTheRestInTheStockroom() {
        stockroom.restock(shelf);
        assertThat(stockroom.getReserve(), hasSize(1));
    }

    @Test
    public void takingAnItemSellsTheOneOnTheShelf() {
        stockroom.restock(shelf);
        Item sold = shelf.take("Aged Brie");
        assertThat(sold.getQuality(), is(10));
    }

    @Test
    public void takingAnItemBringsAReplacementFromTheStockroom() {
        stockroom.restock(shelf);
        shelf.take("Aged Brie");
        assertThat(shelf.getItems(), hasSize(2));
        assertThat(stockroom.getReserve(), is(empty()));
    }
}
