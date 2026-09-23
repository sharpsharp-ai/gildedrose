package de.sharpsharp.gildedrose.smells.primitiveobsession;

import java.util.ArrayList;
import java.util.List;

public class ShippingLabel {

    public static List<String> linesFor(Supplier supplier) {
        List<String> lines = new ArrayList<>();
        lines.add(supplier.getName());
        lines.add(supplier.getStreet() + " " + supplier.getHouseNumber());
        lines.add(supplier.getZip() + " " + supplier.getCity());
        if (!"AT".equals(supplier.getCountry())) {
            lines.add(supplier.getCountry());
        }
        return lines;
    }
}
