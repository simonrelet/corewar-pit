package corewar.pit.instruction;

public class TextInstruction extends Instruction {

	private final String text;

	public TextInstruction(String name, String text) {
		super(name, name + " \"" + text + "\"");
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
