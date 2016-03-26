package corewar.shared;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public final class OptionParser {

	private OptionParser() {
	}

	public static Optional<Options> parse(String[] args) {
		boolean json = false;
		String file = "";
		try {
			checkArgument(args.length > 0);
			for (String arg : args) {
				if ("-j".equals(arg)) {
					json = true;
				} else {
					file = arg;
				}
			}
		} catch (Throwable t) {
			return Optional.empty();
		}
		return Optional.of(new Options(json, file));
	}

	public static final class Options {

		private final boolean json;
		private final String file;

		private Options(boolean json, String file) {
			this.json = json;
			this.file = file;
		}

		public static Options defaultOptions() {
			return new Options(false, "");
		}

		public boolean isJson() {
			return json;
		}

		public String getFile() {
			return file;
		}
	}
}
