package corewar.pit.instruction;

public class BinaryInstruction extends Instruction {

	private final String opCode;

	public BinaryInstruction(String opCode, String name, String displayLine) {
		super(name, displayLine);
		this.opCode = opCode;
	}

	public String toBinary() {
		return opCode;
	}
}
