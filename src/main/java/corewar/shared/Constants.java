package corewar.shared;

public final class Constants {

	public static final char COMMENT_CHAR = '#';
	public static final String NON_TEXT_PADDING_CHAR = "\0";
	public static final int NAME_MAX_CHARACTER_COUNT = 32;
	public static final int COMMENT_MAX_CHARACTER_COUNT = 128;

	// Platform dependencies
	public static final String REGEX_END_OF_LINE = "\\r?\\n";

	// Others
	public static final String HEX_PREFIX = "0x";

	private Constants() {
	}
}
