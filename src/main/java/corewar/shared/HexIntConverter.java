package corewar.shared;

public final class HexIntConverter {

	private HexIntConverter() {
	}

	public static int hexToInt(String hex) {
		return hexToIntImpl(hex.startsWith(Constants.HEX_PREFIX) ? hex.substring(2) : hex);
	}

	public static String intToHex(int value, int nbNibbles) {
		return intToHexImpl(value, nbNibbles, false);
	}

	public static String intToHexWithPrefix(int value, int nbNibbles) {
		return intToHexImpl(value, nbNibbles, true);
	}

	public static String intToLittleEndianHex(int value, int nbNibbles) {
		return new StringBuilder(intToHexImpl(value, nbNibbles, false)).reverse().toString();
	}

	private static int hexToIntImpl(String hex) {
		int res = 0;
		byte[] bytes = hex.toLowerCase().getBytes();
		int i = 0;
		for (; i < bytes.length; i++) {
			res |= Utilities.charToByte((char) bytes[bytes.length - i - 1]) << (4 * i);
		}

		int shift = 8 - i;
		if (shift > 0) {
			res <<= shift * 4;
			// Using arithmetic shift to retrieve the correct sign.
			res >>= shift * 4;
		}

		return res;
	}

	private static String intToHexImpl(int value, int nbNibbles, boolean withPrefix) {
		StringBuilder stringBuilder = new StringBuilder(withPrefix ? Constants.HEX_PREFIX : "");
		String hexString = Integer.toHexString(value);
		int hexLength = hexString.length();
		if (hexLength < nbNibbles) {
			for (int i = 0; i < nbNibbles - hexLength; i++) {
				stringBuilder.append("0");
			}
		} else if (hexLength > nbNibbles) {
			hexString = hexString.substring(hexLength - nbNibbles, hexLength);
		}
		stringBuilder.append(hexString);
		return stringBuilder.toString();
	}
}
