package de.sharpsharp.gildedrose;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ABackstagePass {
    private final Item backstagePass = new Item(TestConstants.BACKSTAGE_PASS_NAME, 0, 0);

    private void setTestData(int sellin, int quality) {
        backstagePass.setSellIn(sellin);
        backstagePass.setQuality(quality);
    }
    @Test
    public void with11DaysShouldIncreaseQualityByOne() {
        setTestData(11, 30);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(31));
    }

    @Test
    public void with10DaysShouldIncreaseQualityByTwo() {
        setTestData(10, 30);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(32));
    }

    @Test
    public void with6DaysShouldIncreaseQualityByTwo() {
        setTestData(6, 30);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(32));
    }

    @Test
    public void with5DaysShouldIncreaseQualityByThree() {
        setTestData(5, 30);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(33));
    }

    @Test
    public void shouldHaveZeroQualityWhenTheConcertIsOver() {
        setTestData(0, 30);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(TestConstants.MIN_QUALITY));
    }

    @Test
    public void qualityCannotExceedMaximumQuality() {
        setTestData(5, 50);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(not(greaterThan(50))));
    }

    @Test
    public void qualityCannotExceedMaximumQualityWhen49() {
        setTestData(5, 49);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(not(greaterThan(50))));
    }
    @Test
    public void qualityCannotExceedMinimumQuality() {
        setTestData(-1, 10);
        GildedRose.with(backstagePass).updateQuality();
        assertThat(backstagePass.getQuality(), is(0));
    }

}
