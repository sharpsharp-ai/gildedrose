package de.sharpsharp.gildedrose;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class GildedRoseTest {

    @Test
    public void anItemKeepsItsName() {
        Item foo = new Item("foo", 0, 0);
        GildedRose.with(foo).updateQuality();
        assertThat(foo.getName(), is("foo"));
    }
}
