package de.sharpsharp.gildedrose.approval;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.sharpsharp.gildedrose.TestConstants.*;
import org.approvaltests.Approvals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.sharpsharp.gildedrose.GildedRose;
import de.sharpsharp.gildedrose.Item;

public class ApproveGildedRoseRandom {

    private static final int NUMBER_OF_INPUTS = 20000;
    private static final int MIN_SELLIN_VALUE = -5;
    private static final int MAX_SELLIN_VALUE = 20;
    private static final int MIN_QUALITY_VALUE = MIN_QUALITY;
    private static final int MAX_QUALITY_VALUE = 80;
    private static final String[] names = {
            AGED_BRIE_NAME,
            BACKSTAGE_PASS_NAME,
            SULFURAS_NAME,
            DEXTERITY_VEST_NAME,
            CONJURED_MANA_CAKE_NAME,
            ELIXIR_MONGOOSE_NAME
            };
    public static final int SEED = 202020;

    private Random random;

    @Before
    public void setup() {
        random = new Random(SEED);
    }

    @Test @Ignore
    public void approve() {
        Item[] items = generateItems();
        GildedRose.with(items).updateQuality();
        Approvals.verifyAll("items", items);
    }

    private Item[] generateItems() {
        return Stream.generate(this::createItem)
                .limit(NUMBER_OF_INPUTS).toArray(Item[]::new);
    }

    private Item createItem() {
        return new Item(nextName(), nextSellIn(), nextItemQuality());
    }

    private int nextItemQuality() {
        return MIN_QUALITY_VALUE + random.nextInt(MAX_QUALITY_VALUE - MIN_QUALITY_VALUE);
    }

    private int nextSellIn() {
        return MIN_SELLIN_VALUE + random.nextInt(MAX_SELLIN_VALUE - MIN_SELLIN_VALUE);
    }

    private String nextName() {
        return names[random.nextInt(names.length)];
    }
}
