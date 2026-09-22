---
name: approval-test
description: Golden Master mit ApprovalTests, brute force über ein Raster aus Eingaben, als schnelles Netz vor dem Umbau von Legacy-Code
---
# Approval-Test als Golden Master

Ein Golden Master friert die Ausgabe des Codes über viele Eingaben in einer Datei ein. Er versteht nichts und merkt alles: Jede Verhaltensänderung ist ein Diff. Das Netz steht in Minuten.

| Nr | Regel | Warum |
|---|---|---|
| 1 | Das Raster ist deterministisch: alle Namen × feste `sellIn`-Werte × feste `quality`-Werte | Zufall ohne festen Seed flattert; im Raster sind die Grenzwerte sicher drin |
| 2 | Grenzwerte ins Raster: `sellIn` -1, 0, 1, 4, 5, 6, 9, 10, 11, 15; `quality` 0, 1, 2, 48, 49, 50, 80 | Legacy-Code lebt von `<` und `<=` |
| 3 | Ein Tag je Item; mehrere Tage höchstens als zweiter Test | Sonst verschmiert die Datei die Ursache |
| 4 | Vor dem Genehmigen lesen: Stichproben gegen den Code, dann `scripts/approve.sh` | Genehmigen heißt „so ist es", nicht „so soll es sein" |
| 5 | Erneut genehmigen nur, wenn das Raster sich geändert hat, nie nach einer Änderung am Produktivcode | Sonst genehmigt man den Fehler mit |
| 6 | `*.approved.txt` wird eingecheckt, `*.received.txt` nie (steht in `.gitignore`) | Die genehmigte Datei ist der Test |
| 7 | Das Ausgabeformat bleibt: `Item.toString()` wird nicht angefasst | Eine Formatänderung sähe aus wie eine Verhaltensänderung |

## Muster

```java
package de.sharpsharp.gildedrose;

import java.util.ArrayList;
import java.util.List;

import org.approvaltests.Approvals;
import org.junit.Test;

public class GildedRoseGoldenMasterTest {

    private static final String[] NAMES = {
        "Aged Brie", "Backstage passes to a TAFKAL80ETC concert", "Sulfuras, Hand of Ragnaros",
        "+5 Dexterity Vest", "Conjured Mana Cake"};
    private static final int[] SELL_IN = {-1, 0, 1, 4, 5, 6, 9, 10, 11, 15};
    private static final int[] QUALITY = {0, 1, 2, 48, 49, 50, 80};

    @Test
    public void oneDay() {
        List<Item> items = new ArrayList<>();
        for (String name : NAMES) {
            for (int sellIn : SELL_IN) {
                for (int quality : QUALITY) {
                    items.add(new Item(name, sellIn, quality));
                }
            }
        }
        GildedRose.with(items.toArray(new Item[0])).updateQuality();
        Approvals.verifyAll("items", items.toArray(new Item[0]));
    }
}
```

350 Zeilen, jede Kombination einmal. `Approvals.verifyAll` schreibt `GildedRoseGoldenMasterTest.oneDay.received.txt` neben die Klasse; `scripts/approve.sh` macht daraus die `.approved.txt`.

## Was der Golden Master nicht kann
Er sagt nicht, welche Regel gebrochen ist, und er dokumentiert keine. Dafür gibt es `/characterization-test`. Zusammen: erst das grobe Netz, dann die feinen Tests, dann der Umbau.
