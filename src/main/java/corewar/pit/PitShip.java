package corewar.pit;

public final class PitShip {

	private final String name;
	private final String bin;

	private PitShip(String name, String bin) {
		this.name = name;
		this.bin = bin;
	}

	public static PitShip create(String name, String bin) {
		return new PitShip(name, bin);
	}

	public String getName() {
		return name;
	}

	public String getBin() {
		return bin;
	}
}
