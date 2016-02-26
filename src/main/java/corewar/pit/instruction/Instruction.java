package corewar.pit.instruction;

public abstract class Instruction {

	private final String name;
	protected String displayLine;

	protected Instruction(String name, String displayLine) {
		this.name = name;
		this.displayLine = displayLine;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return displayLine;
	}
}
