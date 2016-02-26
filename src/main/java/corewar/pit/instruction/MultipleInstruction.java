package corewar.pit.instruction;

public class MultipleInstruction extends BinaryInstruction {

	private final int nbRepetitions;

	public MultipleInstruction(String opCode, String name, int nbRepetitions) {
		super(opCode, name, getDisplayLineFromName(name, nbRepetitions));
		this.nbRepetitions = nbRepetitions;
	}

	private static String getDisplayLineFromName(String name, int nbRepetitions) {
		StringBuilder stringBuilder = new StringBuilder(name);
		for (int i = 0; i < nbRepetitions; i++) {
			stringBuilder.append(" ").append(name);
		}
		return stringBuilder.toString();
	}

	public int getNbRepetitions() {
		return nbRepetitions;
	}

	@Override
	public String toBinary() {
		String opCode = super.toBinary();
		StringBuilder stringBuilder = new StringBuilder(opCode);
		for (int i = 0; i < nbRepetitions; i++) {
			stringBuilder.append(opCode);
		}
		return stringBuilder.toString();
	}
}
