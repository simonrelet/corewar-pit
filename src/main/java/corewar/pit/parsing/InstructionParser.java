package corewar.pit.parsing;

import corewar.pit.PitError;
import corewar.pit.PitResult;
import corewar.pit.PitWarning;
import corewar.pit.instruction.ArithmeticInstruction;
import corewar.pit.instruction.BinaryInstruction;
import corewar.pit.instruction.Instruction;
import corewar.pit.instruction.ModeInstruction;
import corewar.pit.instruction.MultipleInstruction;
import corewar.pit.instruction.RegisterInstruction;
import corewar.pit.instruction.TextInstruction;
import corewar.shared.Mode;

import java.util.Map;
import java.util.function.Function;

public final class InstructionParser {

	private static final String STRING_ADDRESS = "address";
	private static final String STRING_REGISTER = "register";
	private static final String STRING_MODE = "mode";
	private static final String STRING_STRING = "string";
	private static final String LABEL_REGEX = "^[a-zA-Z][_a-zA-Z0-9]*(?<!_)$";
	private static final String STRING_REGEX = "^\"(?>[^\\\\\"]++|\\\\.)*+\"$";

	private InstructionParser() {
	}

	public static PitResult<Void> parseLabel(Map<String, Integer> labels, int lineNumber,
			String line, int shipSize, String[] split, String instructionName) {
		PitError error = null;

		if (split.length != 1) {
			error = PitError
					.create(lineNumber, instructionName, line, "A label must be on his own line");
		} else {
			String label = instructionName.substring(0, instructionName.length() - 1);
			if (isValidLabel(label)) {
				if (labels.containsKey(label)) {
					error = PitError.create(lineNumber, label, line, "The label already exists");
				} else {
					labels.put(label, shipSize);
				}
			} else {
				error = PitError.create(lineNumber, label, line,
						"The label is invalid. A label should follow the regexp: " + LABEL_REGEX);
			}
		}
		return PitResult.create(error, (PitWarning) null);
	}

	public static PitResult<Instruction> parseText(InstructionInfo instructionInfo, int lineNumber,
			String lLine, String[] rawParametersList) {
		PitError error = null;
		PitWarning warning = null;
		Instruction result = null;

		String text = String.join(",", rawParametersList);
		if (!isValidString(text)) {
			error = PitError.createInvalid(lineNumber, text, lLine, STRING_STRING);
		} else {
			text = text.substring(1, text.length() - 1);
			int maxCharacterCount = ((TextInstructionInfo) instructionInfo).getMaxCharacterCount();
			if (text.length() > maxCharacterCount) {
				warning = PitWarning.create(lineNumber, text, lLine,
						"Text to long, truncated. The maximum length for text in the " + instructionInfo
								.getName()
								+ " instruction is " + maxCharacterCount);
				text = text.substring(0, maxCharacterCount);
			}
			result = new TextInstruction(instructionInfo.getName(), text.trim());
		}
		return PitResult.create(result, error, warning);
	}

	public static PitResult<Instruction> parseNopCrash(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList) {
		PitError error = null;
		Instruction result = null;

		if (rawParametersList.length > 1) {
			error = PitError.createParameterCount(lineNumber, line, 0);
		} else {
			String[] split = null;
			if (rawParametersList.length == 1) {
				split = rawParametersList[0].split("\\s+");
				for (String s : split) {
					if (!s.toLowerCase().equals(instructionInfo.getName())) {
						error = PitError.create(lineNumber, s, line,
								"Only '" + instructionInfo.getName() + "' were expected");
					}
				}
			}
			if (error == null) {
				result = new MultipleInstruction(((BinaryInstructionInfo) instructionInfo).getOpCode(),
						instructionInfo.getName(), split == null ? 0 : split.length);
			}
		}
		return PitResult.create(result, error);
	}

	public static PitResult<Instruction> parseLdr(InstructionInfo instructionInfo, int lineNumber,
			String line, String[] rawParametersList) {
		return parseLdrStr(instructionInfo, lineNumber, line, rawParametersList, false);
	}

	public static PitResult<Instruction> parseStr(InstructionInfo instructionInfo, int lineNumber,
			String line, String[] rawParametersList) {
		return parseLdrStr(instructionInfo, lineNumber, line, rawParametersList, true);
	}

	public static PitResult<Instruction> parseMode(InstructionInfo instructionInfo, int lineNumber,
			String line, String[] rawParametersList) {
		PitError error = null;
		Instruction result = null;

		if (rawParametersList.length != 1) {
			error = PitError.createParameterCount(lineNumber, line, 1);
		} else {
			String modeValue = rawParametersList[0].trim();
			Mode mode = Mode.fromValue(modeValue);
			if (mode == null) {
				try {
					mode = Mode.fromValue(Integer.parseInt(modeValue));
				} catch (NumberFormatException e) {
					mode = null;
				}
				if (mode == null) {
					error = PitError.createInvalid(lineNumber, modeValue, line, STRING_MODE);
				}
			}
			if (error == null) {
				result = new ModeInstruction(mode);
			}
		}

		return PitResult.create(result, error);
	}

	public static PitResult<Instruction> parse0Parameters(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList) {
		PitError error = null;
		Instruction result = null;

		if (rawParametersList.length != 0) {
			error = PitError.createParameterCount(lineNumber, line, 0);
		} else {
			result = new BinaryInstruction(((BinaryInstructionInfo) instructionInfo).getOpCode(),
					instructionInfo.getName(), instructionInfo.getName());
		}

		return PitResult.create(result, error);
	}

	public static PitResult<Instruction> parse1Parameters(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList) {
		return parseOnlyRegisters(instructionInfo, lineNumber, line, rawParametersList, 1);
	}

	public static PitResult<Instruction> parse2ParametersRegReg(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList) {
		return parseOnlyRegisters(instructionInfo, lineNumber, line, rawParametersList, 2);
	}

	public static PitResult<Instruction> parse2ParametersRegNum(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList) {
		return parseArithmetic(instructionInfo, lineNumber, line, rawParametersList, false, 1);
	}

	public static PitResult<Instruction> parse3Parameters(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList) {
		return parseArithmetic(instructionInfo, lineNumber, line, rawParametersList, true, 2);
	}

	private static PitResult<Instruction> parseLdrStr(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList, boolean firstRegIsAddress) {
		PitError error = null;
		Instruction result = null;
		int[] registers = new int[2];
		boolean[] addresses = new boolean[2];

		if (rawParametersList.length != 2) {
			error = PitError.createParameterCount(lineNumber, line, 2);
		} else {
			RegisterFunction[] registerFunctions = new RegisterFunction[2];
			String[] typeString = new String[2];
			if (firstRegIsAddress) {
				registerFunctions[0] = InstructionParser::getAddressValue;
				registerFunctions[1] = InstructionParser::getRegisterValue;
				typeString[0] = STRING_ADDRESS;
				typeString[1] = STRING_REGISTER;
				addresses[0] = true;
				addresses[1] = false;
			} else {
				registerFunctions[0] = InstructionParser::getRegisterValue;
				registerFunctions[1] = InstructionParser::getAddressValue;
				typeString[0] = STRING_REGISTER;
				typeString[1] = STRING_ADDRESS;
				addresses[0] = false;
				addresses[1] = true;
			}

			for (int i = 0; i < 2 && error == null; i++) {
				String registerStr = rawParametersList[i].trim();
				int registerValue = registerFunctions[i].apply(registerStr);
				if (registerValue == -1) {
					error = PitError.createInvalid(lineNumber, registerStr, line, typeString[i]);
				}
				registers[i] = registerValue;
			}
		}
		if (error == null) {
			result = new RegisterInstruction(((BinaryInstructionInfo) instructionInfo).getOpCode(),
					instructionInfo.getName(), registers, addresses);
		}
		return PitResult.create(result, error);
	}

	private static PitResult<Instruction> parseOnlyRegisters(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList, int nbRegisters) {
		PitError error = null;
		Instruction result = null;

		if (rawParametersList.length != nbRegisters) {
			error = PitError.createParameterCount(lineNumber, line, nbRegisters);
		} else {
			int[] registers = new int[nbRegisters];
			for (int i = 0; i < nbRegisters && error == null; i++) {
				String registerStr = rawParametersList[i].trim();
				int registerValue = getRegisterValue(registerStr);
				if (registerValue == -1) {
					error = PitError.createInvalid(lineNumber, registerStr, line, STRING_REGISTER);
				}
				registers[i] = registerValue;
			}
			if (error == null) {
				result = new RegisterInstruction(((BinaryInstructionInfo) instructionInfo).getOpCode(),
						instructionInfo.getName(), registers);
			}
		}
		return PitResult.create(result, error);
	}

	private static PitResult<Instruction> parseArithmetic(InstructionInfo instructionInfo,
			int lineNumber, String line, String[] rawParametersList, boolean address,
			int nbArithmetic) {
		PitError error = null;
		Instruction result = null;

		if (rawParametersList.length != nbArithmetic + 1) {
			error = PitError.createParameterCount(lineNumber, line, nbArithmetic + 1);
		} else {
			String registerStr = rawParametersList[0].trim();
			int registerValue;
			registerValue = address ? getAddressValue(registerStr) : getRegisterValue(registerStr);
			if (registerValue == -1) {
				error = PitError.createInvalid(lineNumber, registerStr, line,
						address ? STRING_ADDRESS : STRING_REGISTER);
			} else {
				String[] arithmetic = new String[nbArithmetic];
				for (int i = 0; i < nbArithmetic; i++) {
					arithmetic[i] = rawParametersList[i + 1].trim();
				}
				int nbNibbles = ((ArithmeticInstructionInfo) InstructionInfo
						.getInstructionInfo(instructionInfo.getName())).getNbNibbles();
				result = new ArithmeticInstruction(
						((BinaryInstructionInfo) instructionInfo).getOpCode(),
						instructionInfo.getName(), lineNumber, line, registerValue, address, arithmetic,
						nbNibbles);
			}
		}
		return PitResult.create(result, error);
	}

	private static boolean isValidLabel(String label) {
		return !label.isEmpty() && label.matches(LABEL_REGEX);
	}

	// -- Parsing utilities ----------------------------------------------------------------------

	private static boolean isValidString(String str) {
		return str.matches(STRING_REGEX);
	}

	private static int getRegisterValue(String registerStr) {
		if (registerStr.length() < 2 || !registerStr.startsWith("r")) {
			return -1;
		}
		int res;
		try {
			res = Integer.parseInt(registerStr.substring(1));
			if (res < 0 || res > 15) {
				res = -1;
			}
		} catch (Exception e) {
			res = -1;
		}
		return res;
	}

	private static int getAddressValue(String registerStr) {
		if (!registerStr.startsWith("[") || !registerStr.endsWith("]")) {
			return -1;
		}
		return getRegisterValue(registerStr.substring(1, registerStr.length() - 1).trim());
	}

	@FunctionalInterface
	private interface RegisterFunction extends Function<String, Integer> {
		// To Avoid warnings
	}
}
