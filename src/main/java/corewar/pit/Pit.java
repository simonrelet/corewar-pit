package corewar.pit;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import corewar.pit.instruction.ArithmeticInstruction;
import corewar.pit.instruction.BinaryInstruction;
import corewar.pit.instruction.Instruction;
import corewar.pit.instruction.MultipleInstruction;
import corewar.pit.instruction.TextInstruction;
import corewar.pit.parsing.ArithmeticInstructionInfo;
import corewar.pit.parsing.InstructionInfo;
import corewar.pit.parsing.InstructionParser;
import corewar.shared.Constants;
import corewar.shared.Logger;
import corewar.shared.OptionParser.Options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Pit {

	private final Collection<Instruction> shipInstructions = new ArrayList<>();
	private final StringBuilder shipBin = new StringBuilder();
	private final Collection<PitError> pitErrors = new ArrayList<>();
	private final Collection<PitWarning> pitWarnings = new ArrayList<>();
	private final Map<String, Integer> labels = new HashMap<>();
	private String shipName = "";
	private String shipComment = "";
	private int shipSize;
	private int currentLineNumber;
	private String currentLine = "";

	private Pit() {
	}

	public static void build(String inputShipText, Options opt) {
		new Pit().buildInternal(inputShipText, opt);
	}

	private void buildInternal(String inputShipText, Options opt) {
		PitShip pitShip = null;
		String[] lines = inputShipText.split(Constants.REGEX_END_OF_LINE);
		for (String line : lines) {
			currentLine = line;
			currentLineNumber++;

			int commentIndex = line.indexOf(Constants.COMMENT_CHAR);
			if (commentIndex >= 0) {
				currentLine = line.substring(0, commentIndex);
			}

			currentLine = currentLine.trim();
			if (!currentLine.isEmpty()) {
				Optional<Instruction> instruction = getInstruction(currentLine);
				if (instruction.isPresent()) {
					shipInstructions.add(instruction.get());
				}
			}
		}
		currentLineNumber++;

		if (shipName.isEmpty()) {
			pitErrors.add(PitError.create(currentLineNumber,
					"A mame should be given with the following instruction: .name \"Awesomeness\""));
		}

		if (pitErrors.isEmpty()) {
			if (shipInstructions.isEmpty()) {
				pitErrors.add(PitError.create(currentLineNumber, "A ship cannot be empty!"));
			} else {
				evalExpr();
				if (pitErrors.isEmpty()) {
					toBin();
					pitShip = PitShip.create(shipName, shipBin.toString());
				}
			}
		}

		Logger.logResult(PitResult.create(pitShip, pitErrors, pitWarnings), opt);
	}

	private void toBin() {
		shipBin.append(shipName);
		shipBin.append(getTextPadding(shipName.length(), Constants.NAME_MAX_CHARACTER_COUNT));

		shipBin.append(shipComment);
		shipBin.append(getTextPadding(shipComment.length(), Constants.COMMENT_MAX_CHARACTER_COUNT));

		shipInstructions.forEach(instruction -> {
			if (instruction instanceof BinaryInstruction) {
				shipBin.append(((BinaryInstruction) instruction).toBinary());
			}
		});
	}

	private static String getTextPadding(int textLength, int maxLength) {
		return Strings.repeat(Constants.NON_TEXT_PADDING_CHAR, maxLength - textLength);
	}

	private void evalExpr() {
		shipInstructions.forEach(instruction -> {
			if (instruction instanceof ArithmeticInstruction) {
				evalExprImpl((ArithmeticInstruction) instruction);
			}
		});
	}

	private void evalExprImpl(ArithmeticInstruction instruction) {
		String[] arithmetic = instruction.getArithmetic();
		int[] values = new int[arithmetic.length];
		for (int i = 0; i < arithmetic.length; i++) {
			PitResult<Integer> result = Eval.eval(labels, arithmetic[i], instruction.getLineNumber(),
					instruction.getLine());
			if (result.hasErrors()) {
				pitErrors.addAll(result.getErrors());
				break;
			}

			int value = result.getResult();
			InstructionInfo instructionInfo = InstructionInfo
					.getInstructionInfo(instruction.getName());
			Preconditions.checkNotNull(instructionInfo, "There should be an instruction");
			int nbNibbles = ((ArithmeticInstructionInfo) instructionInfo).getNbNibbles();
			int pow3 = (int) Math.pow(2, 3 * nbNibbles);
			int maxValue = pow3 - 1;
			int minValue = -pow3;
			int range = (int) Math.pow(2, 4 * nbNibbles);
			if (value < minValue || value > maxValue) {
				value = ((value - minValue) % range) + minValue;
			}
			values[i] = value;
		}
		instruction.setValues(values);
	}

	private Optional<Instruction> getInstruction(String line) {
		String[] split = line.split("\\s+", 2);
		String instructionName = split[0];

		if (instructionName.endsWith(":")) {
			getLabelInstruction(split, instructionName);
			return Optional.empty();
		}

		instructionName = instructionName.toLowerCase();
		InstructionInfo instructionInfo = InstructionInfo.getInstructionInfo(instructionName);
		if (instructionInfo == null) {
			pitErrors.add(PitError.create(currentLineNumber, instructionName, currentLine,
					"Unknown instruction"));
			return Optional.empty();
		}

		String rawParameters = "";
		if (split.length > 1) {
			rawParameters = split[1];
		}

		String[] rawParametersList;
		rawParametersList = rawParameters.isEmpty() ? new String[0] : rawParameters.split(",");

		PitResult<Instruction> result = instructionInfo.parse(currentLineNumber, currentLine,
				rawParametersList);
		if (result.hasErrors()) {
			pitErrors.addAll(result.getErrors());
			return Optional.empty();
		}

		if (result.hasWarnings()) {
			pitWarnings.addAll(result.getWarnings());
		}

		Instruction instruction = result.getResult();
		if (instruction instanceof TextInstruction) {
			TextInstruction textInstruction = (TextInstruction) instruction;
			if (InstructionInfo.NAME.getName().equals(instruction.getName())) {
				shipName = textInstruction.getText();
			} else {
				shipComment = textInstruction.getText();
			}
			return Optional.empty();
		}

		if (instruction instanceof MultipleInstruction) {
			MultipleInstruction multipleInstruction = (MultipleInstruction) instruction;
			shipSize += instructionInfo.getLength() * multipleInstruction.getNbRepetitions();
		}

		shipSize += instructionInfo.getLength();
		return Optional.of(instruction);
	}

	private void getLabelInstruction(String[] split, String instructionName) {
		PitResult<?> result = InstructionParser.parseLabel(labels, currentLineNumber, currentLine,
				shipSize, split, instructionName);
		if (result.hasErrors()) {
			pitErrors.addAll(result.getErrors());
		}
		if (result.hasWarnings()) {
			pitWarnings.addAll(result.getWarnings());
		}
	}
}
