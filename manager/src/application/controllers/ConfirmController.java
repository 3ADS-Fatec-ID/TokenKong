package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ConfirmController {
	
	@FXML public VBox confirm;
	
	@FXML public Label title;
	
	@FXML public Label message;
	
	@FXML public ImageView icon;
	
	@FXML public Button cancelButton;
	
	@FXML public Button confirmButton;
	
	@FXML public void initialize() {}
}