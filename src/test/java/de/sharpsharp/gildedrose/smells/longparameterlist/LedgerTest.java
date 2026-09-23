package de.sharpsharp.gildedrose.smells.longparameterlist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.time.LocalDate;

import org.junit.Test;

public class LedgerTest {

    private final Ledger ledger = new Ledger();
    private final Merchant merchant = new Merchant("Käserei Steirer", "Graz");
    private final LocalDate ordered = LocalDate.of(2026, 9, 1);
    private final LocalDate delivered = LocalDate.of(2026, 9, 3);

    @Test
    public void anUnpaidDeliveryIsOpen() {
        ledger.record("Aged Brie", 12, 250, merchant.getName(), merchant.getCity(), ordered, delivered, false);
        assertThat(ledger.openInCents(), is(3000));
    }

    @Test
    public void aPaidDeliveryLeavesNothingOpen() {
        ledger.record("Aged Brie", 12, 250, merchant.getName(), merchant.getCity(), ordered, delivered, true);
        assertThat(ledger.openInCents(), is(0));
    }

    @Test
    public void theLineNamesQuantityMerchantDatesAndTotal() {
        ledger.record("Aged Brie", 12, 250, merchant.getName(), merchant.getCity(), ordered, delivered, false);
        assertThat(ledger.lines().get(0),
                is("12 x Aged Brie von Käserei Steirer, Graz, bestellt 2026-09-01, geliefert 2026-09-03: 3000 Cent, offen"));
    }

    @Test
    public void swappedQuantityAndPriceCompileJustFine() {
        ledger.record("Aged Brie", 250, 12, merchant.getName(), merchant.getCity(), ordered, delivered, false);
        assertThat(ledger.lines().get(0), startsWith("250 x Aged Brie"));
    }
}
