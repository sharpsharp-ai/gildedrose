package de.sharpsharp.gildedrose.approval;

import org.approvaltests.Approvals;
import org.junit.Ignore;
import org.junit.Test;

import de.sharpsharp.gildedrose.GildedRose;
import de.sharpsharp.gildedrose.Item;

import static de.sharpsharp.gildedrose.TestConstants.*;

public class ApproveGuildedRose {
    @Test @Ignore
    public void approve() {
        final Item[] items = generateItems();
        GildedRose.with(items).updateQuality();
        Approvals.verifyAll("", items);
    }

    private Item[] generateItems() {
        return new Item[] {
                    new Item("Item", 100, 1),
                    new Item("Item", 0, 10),
                    new Item("Item", 1000, MIN_QUALITY),
                    new Item("Item", 14, MAX_QUALITY),
                    new Item("Item", 0, MIN_QUALITY),
                    new Item(SULFURAS_NAME, 5, 80),
                    new Item(SULFURAS_NAME, -1, 80),
                    new Item(AGED_BRIE_NAME, 5, 15),
                    new Item(AGED_BRIE_NAME, 0, 15),
                    new Item(AGED_BRIE_NAME, 5, MAX_QUALITY),
                    new Item(AGED_BRIE_NAME, 0, MAX_QUALITY),
                    new Item(BACKSTAGE_PASS_NAME, 11, 30),
                    new Item(BACKSTAGE_PASS_NAME, 10, 30),
                    new Item(BACKSTAGE_PASS_NAME, 6, 30),
                    new Item(BACKSTAGE_PASS_NAME, 5, 30),
                    new Item(BACKSTAGE_PASS_NAME, 0, 30),
                    new Item(BACKSTAGE_PASS_NAME, 5, 50),
                    new Item(BACKSTAGE_PASS_NAME, 5, 49),
                    new Item(BACKSTAGE_PASS_NAME, -1, 10),

            };
    }
}
