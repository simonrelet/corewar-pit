package corewar.pit.parsing;

import corewar.shared.InstructionType;

public class ArithmeticInstructionInfo extends BinaryInstructionInfo {

	private final int nbNibbles;

	public ArithmeticInstructionInfo(InstructionType instruction, InstructionParsingFunction parsingFunction, String opCode, int nbNibbles) {
		super(instruction, parsingFunction, opCode);
		this.nbNibbles = nbNibbles;
	}

	public int getNbNibbles() {
		return nbNibbles;
	}
}
