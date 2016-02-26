package corewar.shared;

public enum InstructionType {

	NAME(".name", 0),
	COMMENT(".comment", 0),
	CRASH("crash", 1),
	NOP("nop", 1),
	CHECK("check", 2),
	FORK("fork", 2),
	B("b", 3),
	BZ("bz", 3),
	BNZ("bnz", 3),
	BS("bs", 3),
	WRITE("write", 3),
	MODE("mode", 3),
	AND("and", 3),
	OR("or", 3),
	XOR("xor", 3),
	NOT("not", 3),
	ADD("add", 3),
	SUB("sub", 3),
	CMP("cmp", 3),
	NEG("neg", 3),
	MOV("mov", 3),
	SWP("swp", 4),
	ROL("rol", 3),
	ASR("asr", 3),
	ADDI("addi", 4),
	CMPI("cmpi", 4),
	LC("lc", 5),
	LL("ll", 7),
	STAT("stat", 4),
	LDR("ldr", 3),
	STR("str", 3),
	LDB("ldb", 7),
	STB("stb", 7);


	private final String name;
	private final int size;

	private InstructionType(String name, int size) {
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}
}
