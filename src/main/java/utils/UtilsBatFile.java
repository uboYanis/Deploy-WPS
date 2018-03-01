package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

public class UtilsBatFile {

	public static File createStartBat(File batFile, File wpsFile, File geoserverFile) throws FileNotFoundException {
		File startFile = UtilsBatFile.getFullPathBat(batFile, "start");
		Formatter formatter = cteateFile(startFile);
		UtilsBatFile.addRecords(formatter, UtilsBatFile.getText(wpsFile.toString(), geoserverFile.toString()));
		UtilsBatFile.closeFile(formatter);
		return startFile;
	}

	public static File createShutdownFile(File batFile, File geoserverFile) throws FileNotFoundException {
		File shutDown = UtilsBatFile.getFullPathBat(batFile, "shutdown");
		Formatter formatter = cteateFile(shutDown);
		UtilsBatFile.addRecords(formatter, getShutDownText(geoserverFile));
		UtilsBatFile.closeFile(formatter);
		return shutDown;
	}

	private static Formatter cteateFile(File file) throws FileNotFoundException {
		Formatter formatter = new Formatter(file);
		return formatter;
	}

	private static void addRecords(Formatter formatter, String text) {
		formatter.format("%s", text);
	}

	private static void closeFile(Formatter formatter) {
		formatter.close();
	}

	private static File getFullPathBat(File batFile, String name) {
		return new File(batFile.getAbsolutePath() + File.separator + name + ".bat");
	}

	private static String getText(String wps, String geoServer) {
		return "cd /D " + wps + " \n call mvn clean install \n cd /D " + geoServer + "\n call startup.bat";
	}

	private static String getShutDownText(File geoserverFile) {
		return "cd /D " + geoserverFile.toString() + "\n call shutdown.bat";
	}

}
