package application.components.AlertComponent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AlertController {
	
	@FXML public HBox alert;
	
	@FXML public Label title;
	
	@FXML public Label message;
	
	@FXML public ImageView icon;
	
	@FXML public void initialize() {}
}
