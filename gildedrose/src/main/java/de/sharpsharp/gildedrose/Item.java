package de.sharpsharp.gildedrose;

/**
 * DO NOT TOUCH --
 * However, do not alter the Item class or Items property as those belong to the goblin in the 
 * corner who will insta-rage and one-shot you as he doesn't believe in shared code ownership
 */
public class Item {
    private final String name;
    private int sellIn;
    private int quality; 
    
    public Item(String name, int sellIn, int quality) {
        this.name = name;
        this.setSellIn(sellIn);
        this.setQuality(quality);
    }
    
    public String getName() {
        return name;
    }
    public int getSellIn() {
        return sellIn;
    }
    public void setSellIn(int sellIn) {
        this.sellIn = sellIn;
    }
    public int getQuality() {
        return quality;
    }
    public void setQuality(int quality) {
        this.quality = quality;
    }
    
    @Override
    public String toString() {
    	return getName() + "[sellin=" + getSellIn() + " quality=" + getQuality() + "]";
    }
}
