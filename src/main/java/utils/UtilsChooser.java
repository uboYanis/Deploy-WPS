package utils;

import java.io.File;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class UtilsChooser {

	public static File getFolderChooser(String title, String absolutePath, TextField inputPath, String errorTitle) {
		File result = null;
		try {
			result = UtilsChooser.folderChooser(title, absolutePath);
			if (result != null) {
				inputPath.setText(result.getAbsolutePath());
			}
		} catch (Exception e) {
			BoiteModale.erreur(errorTitle, e.getMessage());
		}
		return result;
	}

	private static File folderChooser(String title, String absolutePath) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(title);
		if (absolutePath != null) {
			File userDirectory = new File(absolutePath);
			directoryChooser.setInitialDirectory(userDirectory);
		}
		return directoryChooser.showDialog(new Stage());
	}
}
