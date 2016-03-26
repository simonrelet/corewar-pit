package corewar.pit;

public final class PitError extends PitNotification {

	private PitError(int line, String cause, String context, String message) {
		super(Type.ERROR, line, cause, context, message);
	}

	public static PitError create(int line, String cause, String context, String message) {
		return new PitError(line, cause, context, message);
	}

	public static PitError create(String message) {
		return create(-1, "", "", message);
	}

	public static PitError create(int line, String message) {
		return create(line, "", "", message);
	}

	public static PitError createInvalid(int line, String cause, String context, String key) {
		return create(line, cause, context, "Invalid " + key);
	}

	public static PitError createParameterCount(int line, String cause, int expectedCount) {
		return create(line, cause, "",
				"Expected " + expectedCount + " parameter" + (expectedCount > 1 ? "s" : ""));
	}
}
