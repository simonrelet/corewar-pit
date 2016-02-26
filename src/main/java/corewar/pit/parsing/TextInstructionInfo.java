package corewar.pit.parsing;

import corewar.pit.PitResult;
import corewar.pit.instruction.Instruction;
import corewar.shared.InstructionType;

public class TextInstructionInfo extends InstructionInfo {

	private final int maxCharacterCount;

	public TextInstructionInfo(InstructionType instruction, InstructionParsingFunction parsingFunction, int maxCharacterCount) {
		super(instruction, parsingFunction);
		this.maxCharacterCount = maxCharacterCount;
	}

	public int getMaxCharacterCount() {
		return maxCharacterCount;
	}

	@Override
	public PitResult<Instruction> parse(int lineNumber, String line, String[] rawParametersList) {
		return parsingFunction.<TextInstructionInfo>parse(this, lineNumber, line, rawParametersList);
	}
}
