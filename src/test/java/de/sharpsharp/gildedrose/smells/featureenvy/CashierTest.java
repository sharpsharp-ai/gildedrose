package de.sharpsharp.gildedrose.smells.featureenvy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class CashierTest {

    private final Cashier cashier = new Cashier();

    @Test
    public void threeNightsCostThreeTimesTheRate() {
        cashier.collect(new Stay(3, 6000, 0));
        assertThat(cashier.drawerInCents(), is(18000));
    }

    @Test
    public void aWeekGetsTenPercentOff() {
        cashier.collect(new Stay(7, 6000, 0));
        assertThat(cashier.drawerInCents(), is(37800));
    }

    @Test
    public void breakfastsAreChargedPerPiece() {
        cashier.collect(new Stay(1, 6000, 2));
        assertThat(cashier.drawerInCents(), is(8400));
    }

    @Test
    public void theDrawerSumsUpEveryStay() {
        cashier.collect(new Stay(1, 6000, 0));
        cashier.collect(new Stay(2, 5000, 0));
        assertThat(cashier.drawerInCents(), is(16000));
    }
}
