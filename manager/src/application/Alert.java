package application;

import java.io.File;

import application.controllers.AlertController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Alert {
	
	public static void initializeAlert(StackPane composition) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader();
			AlertController alertController = new AlertController();
			loader.setController(alertController);
			File file = new File("src\\application\\views\\components\\Alert.fxml");
			loader.setLocation(file.toURI().toURL());
			HBox alert = (HBox)loader.load();
			//snack.setVisible(false);
			composition.getChildren().add(3, alert);
			StackPane.setAlignment(alert, Pos.BOTTOM_RIGHT);
			StackPane.setMargin(alert, new Insets(0, 16, 16, 0));
			alert.setVisible(false);
		}catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}

	public static void showAlert ( Scene scene, String title, String message, String type, int milliseconds ) {
		try {
			StackPane composition = (StackPane)scene.getRoot();
			HBox alert = (HBox)composition.getChildren().get(3);
			
			ImageView alertIcon = (ImageView)alert.getChildren().get(0);
			Label alertTitle = (Label)((VBox)alert.getChildren().get(1)).getChildren().get(0);
			Label alertMessage =  (Label)((VBox)alert.getChildren().get(1)).getChildren().get(1);
			
			String iconPath = "";
			alert.getStyleClass().clear();
			if(type.equals("success")) {
				iconPath = "/application/assets/icons/light/check-bold.png";
				alert.getStyleClass().add("success");
			}else if(type.equals("error")) {
				iconPath = "/application/assets/icons/light/close-thick.png";
				alert.getStyleClass().add("error");
			}else if(type.equals("warning")) {
				iconPath = "/application/assets/icons/light/alert-outline.png";
				alert.getStyleClass().add("warning");
			}else if(type.equals("info")) {
				iconPath = "/application/assets/icons/light/information-outline.png";
				alert.getStyleClass().add("info");
			}
			
			Image icon = new Image(iconPath);
			alertIcon.setImage(icon);
			alertTitle.setText(title);
			alertMessage.setText(message);
		
			new Thread(() -> {
				try {
					alert.setVisible(true);
					Thread.sleep(new Long(milliseconds));
					alert.setVisible(false);
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}).start();
		}catch( Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
