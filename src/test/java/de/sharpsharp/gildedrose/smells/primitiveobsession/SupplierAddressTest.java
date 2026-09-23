package de.sharpsharp.gildedrose.smells.primitiveobsession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class SupplierAddressTest {

    private final Supplier graz = new Supplier("Käserei Steirer", "Herrengasse", "7", "8010", "Graz", "AT");
    private final Supplier lyon = new Supplier("Fromagerie Dupont", "Rue Mercière", "12", "69002", "Lyon", "FR");

    @Test
    public void aDomesticLabelHasThreeLines() {
        assertThat(ShippingLabel.linesFor(graz), contains("Käserei Steirer", "Herrengasse 7", "8010 Graz"));
    }

    @Test
    public void aForeignLabelEndsWithTheCountry() {
        assertThat(ShippingLabel.linesFor(lyon), contains("Fromagerie Dupont", "Rue Mercière 12", "69002 Lyon", "FR"));
    }

    @Test
    public void domesticPostageIsBasePlusEveryFullKilo() {
        assertThat(Postage.centsFor(graz, 2500), is(300 + 2 * 150));
    }

    @Test
    public void foreignPostageCostsMore() {
        assertThat(Postage.centsFor(lyon, 2500), is(900 + 2 * 400));
    }
}
