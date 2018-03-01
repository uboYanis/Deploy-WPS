package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utils.BoiteModale;
import utils.UtilsBatFile;
import utils.UtilsChooser;
import utils.UtilsResourcesFile;

public class DeployController implements Initializable {
	private static final String FOLDER_RESOURCES_NAME = "deployWps";
	private static final String FOLDER_RESOURCES_PATH = "C:/" + FOLDER_RESOURCES_NAME;
	private static final String GEOSERVER_FILE_PATH = FOLDER_RESOURCES_PATH + "/geoserverPath.txt";
	private static final String BATCH_FOLDER_RESOURCES = FOLDER_RESOURCES_PATH + "/batch";

	@FXML
	private TabPane tabPane;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Label labelMessing;

	@FXML
	private Button btnProject;

	@FXML
	private Button btnShutdown;

	@FXML
	private TextField inputProjectPath;

	@FXML
	private TextField inputGeoServerPath;

	@FXML
	private Button btnDeploWps;

	@FXML
	private Button btnChoosePathGeoServer;

	@FXML
	private Label labelPathGeoServer;

	@FXML
	private Button btnChoosePathBatch;

	@FXML
	private Label labelPathBatch;

	// ==================================
	private File fileJavaProjectPath = null;
	private File fileGeoServerPath = null;

	private File fileStartBat = null;
	private File fileShutdownBat = null;

	// ==================================

	private String resultGeo = null;

	/*
	 * ===================================================================
	 * ================== Chooser Button
	 * ===================================================================
	 */
	@FXML
	void getProjectPath(ActionEvent event) {
		fileJavaProjectPath = UtilsChooser.getFolderChooser("Choose java project", null, inputProjectPath,
				"java project error");
		if (fileJavaProjectPath != null) {
			try {
				fileStartBat = UtilsBatFile.createStartBat(new File(BATCH_FOLDER_RESOURCES), fileJavaProjectPath,
						fileGeoServerPath);
				fileShutdownBat = UtilsBatFile.createShutdownFile(new File(BATCH_FOLDER_RESOURCES), fileGeoServerPath);

			} catch (FileNotFoundException e) {
				BoiteModale.erreur("batch file creation", e.getMessage());
			}
		}
	}

	@FXML
	void getChoosePathGeoServer(ActionEvent event) {
		fileGeoServerPath = UtilsChooser.getFolderChooser("Choose GeoServer bin folder", null, inputGeoServerPath,
				"GeoServer error");
		if (fileGeoServerPath != null) {
			try {
				UtilsResourcesFile.writeFile(GEOSERVER_FILE_PATH, fileGeoServerPath.getAbsolutePath());
			} catch (IOException e) {
				if (!anchorPane.getChildren().contains(labelMessing)) {
					anchorPane.getChildren().add(labelMessing);
				}
			}
			if (anchorPane.getChildren().contains(labelMessing)) {
				anchorPane.getChildren().remove(labelMessing);
			}
		}
	}

	/*
	 * =======================================================================
	 * ================== Simple Button
	 * =======================================================================
	 */

	@FXML
	void deployWps(ActionEvent event) {
		try {
			btnDeploWps.setDisable(true);
			btnShutdown.setDisable(false);
			Runtime.getRuntime().exec("cmd /c start " + fileStartBat.getAbsolutePath());
		} catch (IOException e) {
			BoiteModale.erreur("execute Bat start & run", e.getMessage());
		}
	}

	@FXML
	void shutdown(ActionEvent event) {
		try {
			btnDeploWps.setDisable(false);
			btnShutdown.setDisable(true);
			Runtime.getRuntime().exec("cmd /c start " + fileShutdownBat.getAbsolutePath());
		} catch (IOException e) {
			BoiteModale.erreur("execute Bat shutdown", e.getMessage());
		}
	}

	/*
	 * ==================================================================
	 * ================ listener and binding Button
	 * ==================================================================
	 */
	private void listenerInputProjectPath() {
		inputProjectPath.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null && newValue.trim() != "") {
					btnDeploWps.setDisable(false);
				}
			}
		});
	}

	/*
	 * ==================================================================
	 * ================== initialize
	 * ==================================================================
	 */
	public void initialize(URL location, ResourceBundle resources) {

		inputProjectPath.setEditable(false);
		inputGeoServerPath.setEditable(false);

		try {
			Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/appMain.fxml"));
			tabPane.getTabs().add(1, new Tab("View on the map", node));
		} catch (IOException e) {
			e.printStackTrace();
		}

		listenerInputProjectPath();

		btnShutdown.setDisable(true);
		btnDeploWps.setDisable(true);

		try {
			UtilsResourcesFile.createFolder(FOLDER_RESOURCES_PATH);
			UtilsResourcesFile.createFolder(BATCH_FOLDER_RESOURCES);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			resultGeo = UtilsResourcesFile.readFile(GEOSERVER_FILE_PATH);

			if (!resultGeo.trim().isEmpty()) {
				inputGeoServerPath.setText(resultGeo);
				fileGeoServerPath = new File(resultGeo);
				anchorPane.getChildren().remove(labelMessing);
			}
		} catch (IOException e) {
			if (!anchorPane.getChildren().contains(labelMessing)) {
				anchorPane.getChildren().add(labelMessing);
			}
		} catch (NullPointerException e) {
			if (!anchorPane.getChildren().contains(labelMessing)) {
				anchorPane.getChildren().add(labelMessing);
			}
		}
	}

}
