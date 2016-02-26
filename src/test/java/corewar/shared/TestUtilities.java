package corewar.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUtilities {

	@Test
	public void testCharToByte() {
		assertEquals(0x00, Utilities.charToByte('0'));
		assertEquals(0x01, Utilities.charToByte('1'));
		assertEquals(0x02, Utilities.charToByte('2'));
		assertEquals(0x03, Utilities.charToByte('3'));
		assertEquals(0x04, Utilities.charToByte('4'));
		assertEquals(0x05, Utilities.charToByte('5'));
		assertEquals(0x06, Utilities.charToByte('6'));
		assertEquals(0x07, Utilities.charToByte('7'));
		assertEquals(0x08, Utilities.charToByte('8'));
		assertEquals(0x09, Utilities.charToByte('9'));
		assertEquals(0x0a, Utilities.charToByte('a'));
		assertEquals(0x0b, Utilities.charToByte('b'));
		assertEquals(0x0c, Utilities.charToByte('c'));
		assertEquals(0x0d, Utilities.charToByte('d'));
		assertEquals(0x0e, Utilities.charToByte('e'));
		assertEquals(0x0f, Utilities.charToByte('f'));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCharToByteFails() {
		Utilities.charToByte('p');
	}

	@Test
	public void testByteToChar() {
		assertEquals('0', Utilities.byteToChar((byte) 0x00));
		assertEquals('1', Utilities.byteToChar((byte) 0x01));
		assertEquals('2', Utilities.byteToChar((byte) 0x02));
		assertEquals('3', Utilities.byteToChar((byte) 0x03));
		assertEquals('4', Utilities.byteToChar((byte) 0x04));
		assertEquals('5', Utilities.byteToChar((byte) 0x05));
		assertEquals('6', Utilities.byteToChar((byte) 0x06));
		assertEquals('7', Utilities.byteToChar((byte) 0x07));
		assertEquals('8', Utilities.byteToChar((byte) 0x08));
		assertEquals('9', Utilities.byteToChar((byte) 0x09));
		assertEquals('a', Utilities.byteToChar((byte) 0x0a));
		assertEquals('b', Utilities.byteToChar((byte) 0x0b));
		assertEquals('c', Utilities.byteToChar((byte) 0x0c));
		assertEquals('d', Utilities.byteToChar((byte) 0x0d));
		assertEquals('e', Utilities.byteToChar((byte) 0x0e));
		assertEquals('f', Utilities.byteToChar((byte) 0x0f));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBytToCharFails() {
		Utilities.byteToChar((byte) 0x10);
	}
}
