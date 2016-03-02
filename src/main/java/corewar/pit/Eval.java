package corewar.pit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static corewar.shared.Utilities.hexStringToInt;
import static corewar.shared.Utilities.intStringToInt;

public final class Eval {

	private Eval() {
	}

	public static PitResult<Integer> eval(Map<String, Integer> labels, String str, int currentLineNumber, String currentLine) {
		Parser parser = new Parser(labels, str, currentLineNumber, currentLine);
		List<PitError> errors = null;
		Integer d;
		try {
			d = parser.parse();
		} catch (Exception e) {
			errors = parser.getErrors();
			d = null;
		}

		return PitResult.create(d, errors, null);
	}

	private static boolean isNumberOrLabel(int c) {
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_');
	}

	private static boolean isLabel(String value) {
		char c = value.charAt(0);
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	private static boolean isHexadecimal(String value) {
		return value.startsWith("0x");
	}

	private static final class Parser {

		private final Map<String, Integer> labels;
		private final String str;
		private final int currentLineNumber;
		private final String currentLine;
		private final List<PitError> errors = new ArrayList<>();
		private int pos = -1;
		private int c;

		private Parser(Map<String, Integer> labels, String str, int currentLineNumber, String currentLine) {
			this.labels = labels;
			this.str = str;
			this.currentLineNumber = currentLineNumber;
			this.currentLine = currentLine;
		}

		public List<PitError> getErrors() {
			return errors;
		}

		private void eatChar() {
			c = (++pos < str.length()) ? str.charAt(pos) : -1;
		}

		private void eatSpace() {
			while (Character.isWhitespace(c)) {
				eatChar();
			}
		}

		public int parse() {
			eatChar();
			int v = parseExpression();
			if (c != -1) {
				errors.add(PitError.create(currentLineNumber, String.valueOf((char) c), currentLine, "Unexpected character"));
				throw new RuntimeException();
			}
			return v;
		}

		// Grammar:
		// expression = term | expression `+` term | expression `-` term
		// term = factor | term `*` factor | term `/` factor | term brackets
		// factor = brackets | number | factor `^` factor
		// brackets = `(` expression `)`

		private int parseExpression() {
			int v = parseTerm();
			while (true) {
				eatSpace();
				if (c == '+') { // addition
					eatChar();
					v += parseTerm();
				} else if (c == '-') { // subtraction
					eatChar();
					v -= parseTerm();
				} else {
					return v;
				}
			}
		}

		private int parseTerm() {
			int v = parseFactor();
			while (true) {
				eatSpace();
				if (c == '/') { // division
					eatChar();
					v /= parseFactor();
				} else if (c == '*' || c == '(') { // multiplication
					if (c == '*') {
						eatChar();
					}
					v *= parseFactor();
				} else {
					return v;
				}
			}
		}

		private int parseFactor() {
			int v;
			boolean negate = false;
			eatSpace();
			if (c == '(') { // brackets
				eatChar();
				v = parseExpression();
				if (c == ')') {
					eatChar();
				}
			} else { // numbers
				if (c == '+' || c == '-') { // unary plus & minus
					negate = c == '-';
					eatChar();
					eatSpace();
				}
				StringBuilder sb = new StringBuilder();
				while (isNumberOrLabel(c)) {
					sb.append((char) c);
					eatChar();
				}
				if (sb.length() == 0) {
					errors.add(PitError.create(currentLineNumber, String.valueOf((char) c), currentLine, "Unexpected character"));
					throw new RuntimeException();
				}
				String value = sb.toString();
				if (isLabel(value)) {
					if (labels.containsKey(value)) {
						v = labels.get(value);
					} else {
						errors.add(PitError.create(currentLineNumber, value, currentLine, "Label not found"));
						throw new RuntimeException();
					}
				} else {
					try {
						v = isHexadecimal(value) ? hexStringToInt(value) : intStringToInt(value);
					} catch (Exception e) {
						errors.add(PitError.createInvalid(currentLineNumber, value, currentLine, "number format"));
						throw new RuntimeException();
					}
				}
			}
			eatSpace();
			if (c == '^') { // exponentiation
				eatChar();
				v = (int) Math.pow(v, parseFactor());
			}
			if (negate) { // exponentiation has higher priority than unary minus: -3^2=-9
				v = -v;
			}
			return v;
		}
	}
}
