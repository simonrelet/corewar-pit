package corewar.shared;

public final class Utilities {

	private Utilities() {
	}

	public static int hexStringToInt(String string) {
		return Integer.parseInt(string.startsWith("0x") ? string.substring(2) : string, 16);
	}

	public static int intStringToInt(String string) {
		return Integer.parseInt(string);
	}

	public static byte charToByte(char aChar) {
		byte ref;
		if (aChar >= '0' && aChar <= '9') {
			ref = (byte) '0';
		} else if (aChar >= 'a' && aChar <= 'f') {
			ref = (byte) 'a' - 10;
		} else {
			throw new IllegalArgumentException("The char '" + aChar + "' is a not valid hex value");
		}

		return (byte) (aChar - ref);
	}

	public static char byteToChar(byte aByte) {
		byte ref;
		if (aByte >= 0 && aByte <= 9) {
			ref = (byte) '0';
		} else if (aByte > 9 && aByte < 16) {
			ref = (byte) 'a' - 10;
		} else {
			throw new IllegalArgumentException("The byte '" + aByte
					+ "' cannot be converted to a char");
		}

		return (char) (aByte + ref);
	}
}
