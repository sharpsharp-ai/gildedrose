package de.sharpsharp.gildedrose;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class AItem {

	private final Item item = new Item("X",10, 20);
	
	@Test
	public void gettersShouldReturnValue() {
		assertThat(item, allOf(
				hasProperty("name", is("X")),
				hasProperty("sellIn", is(10)), 
				hasProperty("quality", is(20))));
	}
	
	@Test
	public void sellInShouldChange() {
		item.setSellIn(2);
		assertThat(item.getSellIn(), is(2));
	}
	
	@Test
	public void qualityShouldChange() {
		item.setQuality(5);
		assertThat(item.getQuality(), is(5));
	}
	@Test
	public void toStringShouldReturnInfo() {
		assertThat(item.toString(), is("X[sellin=10 quality=20]"));
	}

}
