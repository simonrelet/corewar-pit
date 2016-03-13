package corewar.shared;

import javax.annotation.Nullable;

public enum Mode {

	FEISAR(0),
	GOTEKI45(1),
	AGSYSTEMS(2),
	AURICOM(3),
	ASSEGAI(4),
	PIRANHA(5),
	QIREX(6),
	ICARAS(7),
	ROCKET(8),
	MISSILE(9),
	MINE(10),
	PLASMA(11),
	MINIPLASMA(12);

	private final int value;

	Mode(int value) {
		this.value = value;
	}

	@Nullable
	public static Mode fromValue(int value) {
		return value < 0 || value >= Mode.values().length ? null : Mode.values()[value];
	}

	@Nullable
	public static Mode fromValue(String value) {
		Mode mode;
		try {
			mode = valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			mode = null;
		}
		return mode;
	}

	public static int offsetWithIdx(int offset, int idx) {
		int tmp = (offset + idx) % (2 * idx);
		if (tmp < 0) {
			tmp = 2 * idx + tmp;
		}
		return tmp - idx;
	}

	public int getValue() {
		return value;
	}
}
