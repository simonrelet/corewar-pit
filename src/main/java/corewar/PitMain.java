package corewar;

import corewar.pit.Pit;
import corewar.shared.Logger;
import corewar.shared.OptionParser;
import corewar.shared.OptionParser.Options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public final class PitMain {

	private PitMain() {
	}

	public static void main(String[] args) {
		Optional<Options> options = OptionParser.parse(args);
		if (options.isPresent()) {
			Options opt = options.get();
			try {
				String content = new String(Files.readAllBytes(Paths.get(opt.getFile())));
				Pit.build(content, opt);
			} catch (IOException e) {
				Logger.logError("Cannot read file + '" + opt.getFile() + "'", opt);
			}
		} else {
			Logger.logError("Pit program argument error", Options.defaultOptions());
		}
	}
}
