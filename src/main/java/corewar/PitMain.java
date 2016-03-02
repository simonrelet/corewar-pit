package corewar;

import corewar.pit.Pit;
import corewar.shared.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PitMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			Logger.logError("Pit program argument error");
		} else {
			String file = args[0];
			try {
				String content = new String(Files.readAllBytes(Paths.get(file)));
				Logger.logResult(Pit.build(content));
			} catch (IOException e) {
				Logger.logError("Cannot read file + '" + file + "'");
			}
		}
	}
}
