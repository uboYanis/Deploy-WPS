package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.BoiteModale;

public class MainController implements Initializable {
	@FXML
	private Button input1;

	@FXML
	private Button input2;

	@FXML
	private TextField field1;

	@FXML
	private TextField field2;

	@FXML
	private Button show;

	private File json1 = null;
	private File json2 = null;

	private File fileChooser(String title, String absolutePath, String nameEx, String extension) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		if (absolutePath != null) {
			File userDirectory = new File(absolutePath);
			fileChooser.setInitialDirectory(userDirectory);
		}
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(nameEx, extension));
		return fileChooser.showOpenDialog(new Stage());
	}

	@FXML
	void chooseFile1(ActionEvent event) {
		json1 = fileChooser("choose GeoJson File", null, "GeoJson file", "*.json*");
		try {
			field1.setText(json1.getAbsolutePath());
		} catch (NullPointerException e) {
			field1.clear();
			BoiteModale.erreur("NullPointerException", " please choose a file (GeoJson1)");
		}
	}

	@FXML
	void chooseFile2(ActionEvent event) {
		json2 = fileChooser("choose GeoJson File", null, "GeoJson file", "*.json*");
		try {
			field2.setText(json2.getAbsolutePath());
		} catch (NullPointerException e) {
			field2.clear();
			BoiteModale.erreur("NullPointerException", " please choose a file (GeoJson2)");
		}
	}

	@FXML
	void displayMap(ActionEvent event) {
		try {
			LeafletController.setJson(json1, json2);
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/view/leafletMap.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void desableBntShow() {
		BooleanBinding bindBtn = new BooleanBinding() {
			{
				super.bind(field1.textProperty(), field2.textProperty());
			}

			@Override
			protected boolean computeValue() {
				return (field1.getText().isEmpty() || field2.getText().isEmpty());
			}
		};
		show.disableProperty().bind(bindBtn);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		desableBntShow();
		field1.setEditable(false);
		field2.setEditable(false);
	}
}
