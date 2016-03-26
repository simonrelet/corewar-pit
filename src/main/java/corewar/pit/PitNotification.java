package corewar.pit;

public abstract class PitNotification {

	private final Type type;
	private final int line;
	private final String cause;
	private final String context;
	private final String message;

	public enum Type {
		ERROR("Error"),
		WARNING("Warning");

		private final String name;

		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	protected PitNotification(Type type, int line, String cause, String context, String message) {
		this.type = type;
		this.line = line;
		this.cause = cause;
		this.context = context;
		this.message = message;
	}

	public String toString(boolean json) {
		if (json) {
			return toJsonString();
		}
		return toPrettyString();
	}

	private String toPrettyString() {
		StringBuilder stringBuilder = new StringBuilder(type.getName())
				.append(" line ")
				.append(line == -1 ? 0 : line)
				.append(": ")
				.append(message);

		if (!cause.isEmpty()) {
			stringBuilder.append(". Caused by '")
					.append(cause)
					.append("'");
			if (!context.isEmpty()) {
				stringBuilder.append(" in '")
						.append(context)
						.append("'");
			}
		}

		return stringBuilder.toString();
	}

	private String toJsonString() {
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
