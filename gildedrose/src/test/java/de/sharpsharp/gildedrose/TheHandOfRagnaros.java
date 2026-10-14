package de.sharpsharp.gildedrose;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;


public class TheHandOfRagnaros {

    final Item sulfura = new Item(TestConstants.SULFURAS_NAME, 5, 80);

    private void setTestData(int sellin) {
        sulfura.setSellIn(sellin);
    }
    @Test
    public void shouldNotDecreaseInQuality() {
        setTestData(5);
        GildedRose.with(sulfura).updateQuality();
        assertThat(sulfura.getQuality(), is(greaterThanOrEqualTo(80)));
    }

    @Test
    public void shouldNotDecreaseInQualityWithNegativeSellIn() {
        setTestData(-1);
        GildedRose.with(sulfura).updateQuality();
        assertThat(sulfura.getQuality(), is(greaterThanOrEqualTo(80)));
    }

    @Test
    public void shouldNotGetOlder() {
        setTestData(10);
        GildedRose.with(sulfura).updateQuality();
        assertThat(sulfura.getSellIn(), is(10));
    }

}
