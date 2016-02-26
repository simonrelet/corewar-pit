package corewar.pit.parsing;

import corewar.shared.InstructionType;

public class BinaryInstructionInfo extends InstructionInfo {

	private final String opCode;

	public BinaryInstructionInfo(InstructionType instruction, InstructionParsingFunction parsingFunction, String opCode) {
		super(instruction, parsingFunction);
		this.opCode = opCode;
	}

	public String getOpCode() {
		return opCode;
	}
}
