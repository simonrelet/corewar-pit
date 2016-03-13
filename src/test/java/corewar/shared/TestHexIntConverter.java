package corewar.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestHexIntConverter {

	@Test
	public void testHexToInt() {
		testHexToIntWithPrefix("8", -8);
		testHexToIntWithPrefix("f", -1);
		testHexToIntWithPrefix("0", 0);
		testHexToIntWithPrefix("7", 7);

		testHexToIntWithPrefix("80", -128);
		testHexToIntWithPrefix("ff", -1);
		testHexToIntWithPrefix("00", 0);
		testHexToIntWithPrefix("7f", 127);

		testHexToIntWithPrefix("8000", -32_768);
		testHexToIntWithPrefix("ffff", -1);
		testHexToIntWithPrefix("0000", 0);
		testHexToIntWithPrefix("7fff", 32_767);

		testHexToIntWithPrefix("80000000", Integer.MIN_VALUE);
		testHexToIntWithPrefix("ffffffff", -1);
		testHexToIntWithPrefix("00000000", 0);
		testHexToIntWithPrefix("7fffffff", Integer.MAX_VALUE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalHexChar() {
		HexIntConverter.hexToInt("g");
	}

	private static void testHexToIntWithPrefix(String hex, int value) {
		String signExtension = value >= 0 ? "0" : "f";
		StringBuilder hexToTest = new StringBuilder(hex);

		do {
			assertEquals(value, HexIntConverter.hexToInt(hexToTest.toString()));
			assertEquals(value, HexIntConverter.hexToInt(Constants.HEX_PREFIX + hexToTest));
			hexToTest.insert(0, getNCopiesOf(signExtension, hexToTest.length()));
		} while (hexToTest.length() <= 8);

		assertEquals(value, HexIntConverter.hexToInt(hex));
		assertEquals(value, HexIntConverter.hexToInt("0x" + hex));
	}

	@Test
	public void testIntToHex() {
		testIntToHexWithPrefix(-8, "8");
		testIntToHexWithPrefix(-1, "f");
		testIntToHexWithPrefix(0, "0");
		testIntToHexWithPrefix(7, "7");

		testIntToHexWithPrefix(-128, "80");
		testIntToHexWithPrefix(-1, "ff");
		testIntToHexWithPrefix(0, "00");
		testIntToHexWithPrefix(127, "7f");

		testIntToHexWithPrefix(-32_768, "8000");
		testIntToHexWithPrefix(-1, "ffff");
		testIntToHexWithPrefix(0, "0000");
		testIntToHexWithPrefix(32_767, "7fff");

		testIntToHexWithPrefix(Integer.MIN_VALUE, "80000000");
		testIntToHexWithPrefix(-1, "ffffffff");
		testIntToHexWithPrefix(0, "00000000");
		testIntToHexWithPrefix(Integer.MAX_VALUE, "7fffffff");
	}

	private static void testIntToHexWithPrefix(int value, String hex) {
		assertEquals(hex, HexIntConverter.intToHex(value, hex.length()));
		assertEquals(Constants.HEX_PREFIX + hex,
				HexIntConverter.intToHexWithPrefix(value, hex.length()));
	}

	@Test
	public void testMethodCoherence() {
		for (int i = -8; i < 8; i++) {
			testMethodCoherence(i, 1);
		}

		for (int i = -128; i < 128; i++) {
			testMethodCoherence(i, 2);
		}

		for (int i = -32_768; i < 32_768; i++) {
			testMethodCoherence(i, 4);
		}

		testMethodCoherence(Integer.MIN_VALUE, 8);
		testMethodCoherence(-1, 8);
		testMethodCoherence(0, 8);
		testMethodCoherence(Integer.MAX_VALUE, 8);
	}

	private static void testMethodCoherence(int value, int nbNibbles) {
		assertEquals(value, HexIntConverter.hexToInt(HexIntConverter.intToHex(value, nbNibbles)));
		assertEquals(value,
				HexIntConverter.hexToInt(HexIntConverter.intToHexWithPrefix(value, nbNibbles)));
	}

	private static String getNCopiesOf(String s, int copies) {
		StringBuilder stringBuilder = new StringBuilder(s);
		for (int i = 1; i < copies; i++) {
			stringBuilder.append(s);
		}
		return stringBuilder.toString();
	}
}
