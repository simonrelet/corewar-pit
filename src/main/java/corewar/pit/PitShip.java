package corewar.pit;

import corewar.pit.instruction.Instruction;

import java.util.List;

public final class PitShip {
//    public final double MAX_SHIP_SIZE = 2098;

	private final String name;
	private final String comment;
	private final String bin;
	private final List<Instruction> instructions;
	private final double size;

	private PitShip(String name, String comment, String bin, List<Instruction> instructions, double size) {
		this.name = name;
		this.comment = comment;
		this.bin = bin;
		this.instructions = instructions;
		this.size = size;
	}

	public static PitShip create(String name, String comment, String bin, List<Instruction> instructions, double size) {
		return new PitShip(name, comment, bin, instructions, size);
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public String getBin() {
		return bin;
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}

	public double getSize() {
		return size;
	}
}
