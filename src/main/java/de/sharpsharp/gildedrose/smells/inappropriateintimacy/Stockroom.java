package de.sharpsharp.gildedrose.smells.inappropriateintimacy;

import java.util.ArrayList;
import java.util.List;

import de.sharpsharp.gildedrose.Item;

public class Stockroom {
    private final List<Item> reserve = new ArrayList<>();

    public void deliver(Item item) {
        reserve.add(item);
    }

    public List<Item> getReserve() {
        return reserve;
    }

    public void restock(Shelf shelf) {
        while (shelf.getItems().size() < shelf.getCapacity() && !reserve.isEmpty()) {
            shelf.getItems().add(reserve.remove(0));
        }
    }
}
