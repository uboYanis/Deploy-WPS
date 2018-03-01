package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UtilsResourcesFile {
	public static String readFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String everything = null;
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();
		} finally {
			br.close();
		}
		return everything;
	}

	public static void writeFile(String paths, String text) throws IOException {
		Path path = Paths.get(paths);
		byte[] strToBytes = text.getBytes();

		Files.write(path, strToBytes);
	}

	public static void createFolder(String folderName) throws Exception {

		File theDir = new File(folderName);
		if (!theDir.exists()) {
			theDir.mkdir();
		}
	}

}
