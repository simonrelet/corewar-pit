package corewar.pit.parsing;

import corewar.pit.PitResult;
import corewar.pit.instruction.Instruction;
import corewar.shared.Constants;
import corewar.shared.InstructionType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class InstructionInfo {

	@FunctionalInterface
	public interface InstructionParsingFunction {

		PitResult<Instruction> parse(InstructionInfo instructionInfo, int lineNumber, String line, String[] rawParametersList);
	}

	public static final TextInstructionInfo NAME = new TextInstructionInfo(InstructionType.NAME, InstructionParser::parseText, Constants.NAME_MAX_CHARACTER_COUNT);
	public static final TextInstructionInfo COMMENT = new TextInstructionInfo(InstructionType.COMMENT, InstructionParser::parseText, Constants.COMMENT_MAX_CHARACTER_COUNT);
	public static final BinaryInstructionInfo CRASH = new BinaryInstructionInfo(InstructionType.CRASH, InstructionParser::parseNopCrash, "0");
	public static final BinaryInstructionInfo NOP = new BinaryInstructionInfo(InstructionType.NOP, InstructionParser::parseNopCrash, "1");
	public static final BinaryInstructionInfo CHECK = new BinaryInstructionInfo(InstructionType.CHECK, InstructionParser::parse0Parameters, "fc");
	public static final BinaryInstructionInfo FORK = new BinaryInstructionInfo(InstructionType.FORK, InstructionParser::parse0Parameters, "fe");
	public static final BinaryInstructionInfo B = new BinaryInstructionInfo(InstructionType.B, InstructionParser::parse1Parameters, "f7");
	public static final BinaryInstructionInfo BZ = new BinaryInstructionInfo(InstructionType.BZ, InstructionParser::parse1Parameters, "f8");
	public static final BinaryInstructionInfo BNZ = new BinaryInstructionInfo(InstructionType.BNZ, InstructionParser::parse1Parameters, "f9");
	public static final BinaryInstructionInfo BS = new BinaryInstructionInfo(InstructionType.BS, InstructionParser::parse1Parameters, "fa");
	public static final BinaryInstructionInfo WRITE = new BinaryInstructionInfo(InstructionType.WRITE, InstructionParser::parse1Parameters, "ff");
	public static final BinaryInstructionInfo MODE = new BinaryInstructionInfo(InstructionType.MODE, InstructionParser::parseMode, "fd");
	public static final BinaryInstructionInfo AND = new BinaryInstructionInfo(InstructionType.AND, InstructionParser::parse2ParametersRegReg, "2");
	public static final BinaryInstructionInfo OR = new BinaryInstructionInfo(InstructionType.OR, InstructionParser::parse2ParametersRegReg, "3");
	public static final BinaryInstructionInfo XOR = new BinaryInstructionInfo(InstructionType.XOR, InstructionParser::parse2ParametersRegReg, "4");
	public static final BinaryInstructionInfo NOT = new BinaryInstructionInfo(InstructionType.NOT, InstructionParser::parse2ParametersRegReg, "5");
	public static final BinaryInstructionInfo ADD = new BinaryInstructionInfo(InstructionType.ADD, InstructionParser::parse2ParametersRegReg, "8");
	public static final BinaryInstructionInfo SUB = new BinaryInstructionInfo(InstructionType.SUB, InstructionParser::parse2ParametersRegReg, "9");
	public static final BinaryInstructionInfo CMP = new BinaryInstructionInfo(InstructionType.CMP, InstructionParser::parse2ParametersRegReg, "a");
	public static final BinaryInstructionInfo NEG = new BinaryInstructionInfo(InstructionType.NEG, InstructionParser::parse2ParametersRegReg, "b");
	public static final BinaryInstructionInfo MOV = new BinaryInstructionInfo(InstructionType.MOV, InstructionParser::parse2ParametersRegReg, "c");
	public static final BinaryInstructionInfo SWP = new BinaryInstructionInfo(InstructionType.SWP, InstructionParser::parse2ParametersRegReg, "f4");
	public static final ArithmeticInstructionInfo ROL = new ArithmeticInstructionInfo(InstructionType.ROL, InstructionParser::parse2ParametersRegNum, "6", 1);
	public static final ArithmeticInstructionInfo ASR = new ArithmeticInstructionInfo(InstructionType.ASR, InstructionParser::parse2ParametersRegNum, "7", 1);
	public static final ArithmeticInstructionInfo ADDI = new ArithmeticInstructionInfo(InstructionType.ADDI, InstructionParser::parse2ParametersRegNum, "f5", 1);
	public static final ArithmeticInstructionInfo CMPI = new ArithmeticInstructionInfo(InstructionType.CMPI, InstructionParser::parse2ParametersRegNum, "f6", 1);
	public static final ArithmeticInstructionInfo LC = new ArithmeticInstructionInfo(InstructionType.LC, InstructionParser::parse2ParametersRegNum, "f2", 2);
	public static final ArithmeticInstructionInfo LL = new ArithmeticInstructionInfo(InstructionType.LL, InstructionParser::parse2ParametersRegNum, "f3", 4);
	public static final ArithmeticInstructionInfo STAT = new ArithmeticInstructionInfo(InstructionType.STAT, InstructionParser::parse2ParametersRegNum, "fb", 1);
	public static final BinaryInstructionInfo LDR = new BinaryInstructionInfo(InstructionType.LDR, InstructionParser::parseLdr, "d");
	public static final BinaryInstructionInfo STR = new BinaryInstructionInfo(InstructionType.STR, InstructionParser::parseStr, "e");
	public static final ArithmeticInstructionInfo LDB = new ArithmeticInstructionInfo(InstructionType.LDB, InstructionParser::parse3Parameters, "f0", 2);
	public static final ArithmeticInstructionInfo STB = new ArithmeticInstructionInfo(InstructionType.STB, InstructionParser::parse3Parameters, "f1", 2);

	private static final Map<String, InstructionInfo> map = new HashMap<>();

	static {
		map.put(NAME.getName(), NAME);
		map.put(COMMENT.getName(), COMMENT);
		map.put(CRASH.getName(), CRASH);
		map.put(NOP.getName(), NOP);
		map.put(CHECK.getName(), CHECK);
		map.put(FORK.getName(), FORK);
		map.put(B.getName(), B);
		map.put(BZ.getName(), BZ);
		map.put(BNZ.getName(), BNZ);
		map.put(BS.getName(), BS);
		map.put(WRITE.getName(), WRITE);
		map.put(MODE.getName(), MODE);
		map.put(AND.getName(), AND);
		map.put(OR.getName(), OR);
		map.put(XOR.getName(), XOR);
		map.put(NOT.getName(), NOT);
		map.put(ADD.getName(), ADD);
		map.put(SUB.getName(), SUB);
		map.put(CMP.getName(), CMP);
		map.put(NEG.getName(), NEG);
		map.put(MOV.getName(), MOV);
		map.put(SWP.getName(), SWP);
		map.put(ROL.getName(), ROL);
		map.put(ASR.getName(), ASR);
		map.put(ADDI.getName(), ADDI);
		map.put(CMPI.getName(), CMPI);
		map.put(LC.getName(), LC);
		map.put(LL.getName(), LL);
		map.put(STAT.getName(), STAT);
		map.put(LDR.getName(), LDR);
		map.put(STR.getName(), STR);
		map.put(LDB.getName(), LDB);
		map.put(STB.getName(), STB);
	}

	protected final InstructionParsingFunction parsingFunction;
	private final InstructionType instructionType;

	protected InstructionInfo(InstructionType instructionType, InstructionParsingFunction parsingFunction) {
		this.instructionType = instructionType;
		this.parsingFunction = parsingFunction;
	}

	@Nullable
	public static InstructionInfo getInstructionInfo(String instructionName) {
		return map.get(instructionName);
	}

	public PitResult<Instruction> parse(int lineNumber, String line, String[] rawParametersList) {
		return parsingFunction.parse(this, lineNumber, line, rawParametersList);
	}

	public String getName() {
		return instructionType.getName();
	}

	public int getLength() {
		return instructionType.getSize();
	}
}
