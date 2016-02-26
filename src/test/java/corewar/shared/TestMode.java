package corewar.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMode {

	@Test
	public void testOffset() {
		assertEquals(1, Mode.offsetWithIdx(1, 2));
		assertEquals(-2, Mode.offsetWithIdx(2, 2));
		assertEquals(-1, Mode.offsetWithIdx(7, 2));
		assertEquals(-1, Mode.offsetWithIdx(-1, 2));
		assertEquals(-2, Mode.offsetWithIdx(-2, 2));
		assertEquals(1, Mode.offsetWithIdx(-3, 2));
		assertEquals(1, Mode.offsetWithIdx(-7, 2));
	}
}
