package corewar.pit;

public abstract class PitNotification {

	private final int line;
	private final String cause;
	private final String context;
	private final String message;

	protected PitNotification(int line, String cause, String context, String message) {
		this.line = line;
		this.cause = cause;
		this.context = context;
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("{\"line\":")
				.append(line == -1 ? 0 : line)
				.append(",\"message\":\"")
				.append(secureString(message));

		if (!cause.isEmpty()) {
			stringBuilder.append(". Caused by '")
					.append(secureString(cause))
					.append("'");
			if (!context.isEmpty()) {
				stringBuilder.append(" in '")
						.append(secureString(context))
						.append("'");
			}
		}

		return stringBuilder
				.append("\"}")
				.toString();
	}

	private static String secureString(String str) {
		return str.replaceAll("\\\\", "\\\\\\\\")
				.replace("\"", "\\\"")
				.replaceAll("\\s+", " ");
	}
}
