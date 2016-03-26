package corewar.shared;

import corewar.pit.PitError;
import corewar.pit.PitResult;
import corewar.pit.PitShip;
import corewar.pit.PitWarning;
import corewar.shared.OptionParser.Options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Logger {

	private Logger() {
	}

	public static void logError(String msg, Options opt) {
		String res;
		if (opt.isJson()) {
			res = "{\"errors\":[{\"line\":0,\"message\":\"" + msg + "\"}]}";
		} else {
			res = "Error: " + msg + "\n";
		}
		System.out.println(res);
	}

	public static void logResult(PitResult<PitShip> result, Options opt) {
		String res;
		if (opt.isJson()) {
			res = TypedLogger.json(result);
		} else {
			res = TypedLogger.pretty(result);
		}
		System.out.println(res);
	}

	private abstract static class TypedLogger {

		public static String json(PitResult<PitShip> result) {
			return new JsonLogger().logResult(result);
		}

		public static String pretty(PitResult<PitShip> result) {
			return new PrettyLogger().logResult(result);
		}

		public String logResult(PitResult<PitShip> result) {
			StringBuilder stringBuilder = prefix(new StringBuilder());
			if (result.hasErrors()) {
				handleErrors(stringBuilder, result.getErrors());
			} else {
				handleShip(stringBuilder, result.getResult());
			}
			if (result.hasWarnings()) {
				handleWarnings(stringBuilder, result.getWarnings());
			}
			return suffix(stringBuilder).toString();
		}

		protected abstract void handleWarnings(StringBuilder stringBuilder,
				List<PitWarning> warnings);

		protected abstract void handleShip(StringBuilder stringBuilder, PitShip result);

		protected abstract void handleErrors(StringBuilder stringBuilder, List<PitError> errors);

		protected abstract StringBuilder suffix(StringBuilder stringBuilder);

		protected abstract StringBuilder prefix(StringBuilder stringBuilder);
	}

	private static class JsonLogger extends TypedLogger {

		@Override
		protected void handleWarnings(StringBuilder stringBuilder, List<PitWarning> warnings) {
			stringBuilder.append(",")
					.append("\"warnings\":[")
					.append(warnings.stream().map(warning -> warning.toString(true))
							.reduce((s, s2) -> s + "," + s2).get())
					.append("]");
		}

		@Override
		protected void handleShip(StringBuilder stringBuilder, PitShip result) {
			stringBuilder.append("\"value\":\"")
					.append(result.getBin())
					.append("\"");
		}

		@Override
		protected void handleErrors(StringBuilder stringBuilder, List<PitError> errors) {
			stringBuilder.append("\"errors\":[")
					.append(errors.stream().map(error -> error.toString(true))
							.reduce((s, s2) -> s + "," + s2).get())
					.append("]");
		}

		@Override
		protected StringBuilder suffix(StringBuilder stringBuilder) {
			return stringBuilder.append("}");
		}

		@Override
		protected StringBuilder prefix(StringBuilder stringBuilder) {
			return stringBuilder.append("{");
		}
	}

	private static class PrettyLogger extends TypedLogger {

		@Override
		protected void handleWarnings(StringBuilder stringBuilder, List<PitWarning> warnings) {
			stringBuilder.append("\n")
					.append(warnings.stream().map(warning -> warning.toString(false))
							.reduce((s, s2) -> s + "\n" + s2).get());
		}

		@Override
		protected void handleShip(StringBuilder stringBuilder, PitShip result) {
			stringBuilder.append(result.getName())
					.append(" is ready to kick asses!\n")
					.append("-- Begin Bin --\n")
					.append(getParts(result.getBin(), 80).stream()
							.reduce((s, s2) -> s + "\n" + s2).get())
					.append("\n--  End Bin  --");
		}

		private static Collection<String> getParts(String string, int partitionSize) {
			List<String> parts = new ArrayList<String>();
			int len = string.length();
			for (int i = 0; i < len; i += partitionSize) {
				parts.add(string.substring(i, Math.min(len, i + partitionSize)));
			}
			return parts;
		}

		@Override
		protected void handleErrors(StringBuilder stringBuilder, List<PitError> errors) {
			stringBuilder.append("Go fix your ship!\n")
					.append(errors.stream().map(error -> error.toString(false))
							.reduce((s, s2) -> s + "\n" + s2).get());
		}

		@Override
		protected StringBuilder suffix(StringBuilder stringBuilder) {
			return stringBuilder.append("\n");
		}

		@Override
		protected StringBuilder prefix(StringBuilder stringBuilder) {
			return stringBuilder;
		}
	}
}
