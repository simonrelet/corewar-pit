package corewar.pit;

public final class PitWarning extends PitNotification {

	private PitWarning(int line, String cause, String context, String message) {
		super(Type.WARNING, line, cause, context, message);
	}

	public static PitWarning create(int line, String cause, String context, String message) {
		return new PitWarning(line, cause, context, message);
	}
}
