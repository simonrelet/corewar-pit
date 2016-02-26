package corewar.shared;

import corewar.pit.PitError;
import corewar.pit.PitNotification;
import corewar.pit.PitResult;
import corewar.pit.PitShip;

import java.util.List;
import java.util.Optional;

public class Logger {

	private Logger() {
	}

	public static void logError(String msg) {
		System.out.println("{errors:[{line:0,msg:\"" + msg + "\"}]}");
	}

	public static void logErrors(List<PitError> errors) {
		Optional<String> res = errors.stream().map(PitNotification::toString).reduce((s, s2) -> s + "," + s2);
		System.out.println("{errors:[" + res.get() + "]}");
	}

	public static void logResult(String value) {
		System.out.println("{value:\"" + value + "\"}");
	}

	public static void logResult(PitResult<PitShip> result) {
		if (result.hasErrors()) {
			logErrors(result.getErrors());
		} else {
			logResult(result.getResult().getBin());
		}
	}
}
