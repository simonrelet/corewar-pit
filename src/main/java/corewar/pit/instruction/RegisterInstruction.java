package corewar.pit.instruction;

import corewar.shared.HexIntConverter;

public class RegisterInstruction extends BinaryInstruction {

	private static final boolean[] DEFAULT_ADDRESSES = {false, false};

	private final int[] registers;
	private final boolean[] addresses;

	public RegisterInstruction(String opCode, String name, int[] registers) {
		this(opCode, name, registers, DEFAULT_ADDRESSES);
	}

	public RegisterInstruction(String opCode, String name, int[] registers, boolean[] addresses) {
		super(opCode, name, getDisplayFromRegisters(name, registers, addresses));
		this.registers = registers;
		this.addresses = addresses;
	}

	@Override
	public String toBinary() {
		StringBuilder stringBuilder = new StringBuilder(super.toBinary());
		for (int register : registers) {
			stringBuilder.append(HexIntConverter.intToHex(register, 1));
		}
		return stringBuilder.toString();
	}

	private static String getDisplayFromRegisters(String name, int[] registers, boolean[] addresses) {
		StringBuilder stringBuilder = new StringBuilder(name).append(" ");
		appendRegisterOrAddress(registers[0], addresses[0], stringBuilder);
		if (registers.length > 1) {
			stringBuilder.append(", ");
			appendRegisterOrAddress(registers[1], addresses[1], stringBuilder);
		}
		return stringBuilder.toString();
	}

	private static void appendRegisterOrAddress(int register, boolean address, StringBuilder stringBuilder) {
		if (address) {
			stringBuilder.append("[");
		}
		stringBuilder.append("r").append(register);
		if (address) {
			stringBuilder.append("]");
		}
	}
}
