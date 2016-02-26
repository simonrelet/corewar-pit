package corewar.pit.instruction;

import corewar.shared.HexIntConverter;

import javax.annotation.Nullable;

public class ArithmeticInstruction extends RegisterInstruction {

	private final int lineNumber;
	private final String line;

	private final String[] arithmetic;
	private final int nbNibbles;
	@Nullable
	private int[] values;

	public ArithmeticInstruction(String opCode, String name, int lineNumber, String line, int register, boolean address, String[] arithmetic, int nbNibbles) {
		super(opCode, name, toIntArray(register), toBooleanArray(address));
		this.lineNumber = lineNumber;
		this.line = line;
		this.arithmetic = arithmetic;
		this.nbNibbles = nbNibbles;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getLine() {
		return line;
	}

	public String[] getArithmetic() {
		return arithmetic;
	}

	public void setValues(@Nullable int[] values) {
		this.values = values;
		if (values != null) {
			StringBuilder stringBuilder = new StringBuilder(displayLine);
			for (int value : values) {
				stringBuilder.append(", ").append(HexIntConverter.intToHexWithPrefix(value, nbNibbles));
			}
			displayLine = stringBuilder.toString();
		}
	}

	@Override
	public String toBinary() {
		StringBuilder stringBuilder = new StringBuilder(super.toBinary());
		if (values != null) {
			for (int value : values) {
				stringBuilder.append(HexIntConverter.intToLittleEndianHex(value, nbNibbles));
			}
		}
		return stringBuilder.toString();
	}

	private static boolean[] toBooleanArray(boolean address) {
		boolean[] array = new boolean[1];
		array[0] = address;
		return array;
	}

	private static int[] toIntArray(int register) {
		int[] array = new int[1];
		array[0] = register;
		return array;
	}
}
