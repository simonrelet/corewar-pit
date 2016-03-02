package corewar.pit.instruction;

import corewar.shared.HexIntConverter;
import corewar.shared.Mode;

import static corewar.pit.parsing.InstructionInfo.MODE;

public class ModeInstruction extends BinaryInstruction {

	private final Mode mode;

	public ModeInstruction(Mode mode) {
		super(MODE.getOpCode(), MODE.getName(), MODE.getName() + " " + HexIntConverter.intToHexWithPrefix(mode.getValue(), 1));
		this.mode = mode;
	}

	@Override
	public String toBinary() {
		return super.toBinary() + HexIntConverter.intToHex(mode.getValue(), 1);
	}
}
