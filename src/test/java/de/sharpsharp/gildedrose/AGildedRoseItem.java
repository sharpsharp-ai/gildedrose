package de.sharpsharp.gildedrose;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class AGildedRoseItem {
    private final Item item = new Item("Item", 0, 0);

    private void setTestData(int sellin, int quality) {
        item.setSellIn(sellin);
        item.setQuality(quality);
    }

    @Test
    public void atEndOfTheDayItemQualityShouldBeDecreasedByOne() {
        setTestData(100, 1);
        GildedRose.with(item).updateQuality();
        assertThat(item.getQuality(), is(0));
    }

    @Test
    public void atEndOfTheDayItemSellInShouldBeDecreasedByOne() {
        setTestData(100, 1);
        GildedRose.with(item).updateQuality();
        assertThat(item.getSellIn(), is(99));
    }

    @Test
    public void atEndOfTheDayItemQualityShouldBeDecreasedByTwoWhenSellInDateHasPassed() {
        setTestData(0, 10);
        GildedRose.with(item).updateQuality();
        assertThat(item.getQuality(), is(8));
    }

    @Test
    public void qualityCanNeverBeDecreasedBelowMinimumQuality() {
        setTestData(1000, TestConstants.MIN_QUALITY);
        GildedRose.with(item).updateQuality();
        assertThat(item.getQuality(), is(greaterThanOrEqualTo(TestConstants.MIN_QUALITY)));
    }

    @Test
    public void qualityCannotExceedMaximumQuality() {
        setTestData(14, TestConstants.MAX_QUALITY);
        GildedRose.with(item).updateQuality();
        assertThat(item.getQuality(), is(not(greaterThan(TestConstants.MAX_QUALITY))));
    }

    @Test
    public void qualityCannotExceedZeroWithNegativeSellIn() {
        setTestData(0, TestConstants.MIN_QUALITY);
        GildedRose.with(item).updateQuality();
        assertThat(item.getQuality(), is(not(lessThan(TestConstants.MIN_QUALITY))));
    }


}
