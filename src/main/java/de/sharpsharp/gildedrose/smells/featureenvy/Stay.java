package de.sharpsharp.gildedrose.smells.featureenvy;

public class Stay {
    private final int nights;
    private final int rateInCents;
    private final int breakfasts;

    public Stay(int nights, int rateInCents, int breakfasts) {
        this.nights = nights;
        this.rateInCents = rateInCents;
        this.breakfasts = breakfasts;
    }

    public int getNights() {
        return nights;
    }

    public int getRateInCents() {
        return rateInCents;
    }

    public int getBreakfasts() {
        return breakfasts;
    }
}
