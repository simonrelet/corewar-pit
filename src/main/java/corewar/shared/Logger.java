package corewar.shared;

import corewar.pit.PitError;
import corewar.pit.PitNotification;
import corewar.pit.PitResult;
import corewar.pit.PitShip;
import corewar.pit.PitWarning;

import java.util.Collection;

public final class Logger {

	private Logger() {
	}

	public static void logError(String msg) {
		System.out.println("{\"errors\":[{\"line\":0,\"msg\":\"" + msg + "\"}]}");
	}

	public static void logResult(PitResult<PitShip> result) {
		StringBuilder stringBuilder = new StringBuilder("{");

		if (result.hasErrors()) {
			stringBuilder.append(getErrors(result.getErrors()));
		} else {
			stringBuilder.append(getValue(result.getResult().getBin()));
		}

		if (result.hasWarnings()) {
			stringBuilder.append(",")
					.append(getWarnings(result.getWarnings()));
		}

		System.out.println(stringBuilder.append("}").toString());
	}

	private static String getWarnings(Collection<PitWarning> warnings) {
		return "\"warnings\":["
				+ warnings.stream().map(PitNotification::toString)
				.reduce((s, s2) -> s + "," + s2).get()
				+ "]";
	}

	private static String getValue(String bin) {
		return "\"value\":\"" + bin + "\"";
	}

	private static String getErrors(Collection<PitError> errors) {
		return "\"errors\":["
				+ errors.stream().map(PitNotification::toString).reduce((s, s2) -> s + "," + s2).get()
				+ "]";
	}
}
