package application;

import java.io.File;

import application.controllers.SnackController;
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

public class Snack {
	
	public static void initializeSnack(StackPane composition) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader();
			SnackController snackController = new SnackController();
			loader.setController(snackController);
			File file = new File("src\\application\\views\\components\\Snack.fxml");
			loader.setLocation(file.toURI().toURL());
			HBox snack = (HBox)loader.load();
			//snack.setVisible(false);
			composition.getChildren().add(3, snack);
			StackPane.setAlignment(snack, Pos.BOTTOM_RIGHT);
			StackPane.setMargin(snack, new Insets(0, 16, 16, 0));
			snack.setVisible(false);
		}catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}

	public static void showSnack ( Scene scene, String title, String message, String type, int milliseconds ) {
		try {
			StackPane composition = (StackPane)scene.getRoot();
			HBox snack = (HBox)composition.getChildren().get(3);
			
			ImageView snackIcon = (ImageView)snack.getChildren().get(0);
			Label snackTitle = (Label)((VBox)snack.getChildren().get(1)).getChildren().get(0);
			Label snackMessage =  (Label)((VBox)snack.getChildren().get(1)).getChildren().get(1);
			
			String iconPath = "";
			snack.getStyleClass().clear();
			if(type.equals("success")) {
				iconPath = "/application/assets/icons/light/check-bold.png";
				snack.getStyleClass().add("success");
			}else if(type.equals("error")) {
				iconPath = "/application/assets/icons/light/close-thick.png";
				snack.getStyleClass().add("error");
			}else if(type.equals("warning")) {
				iconPath = "/application/assets/icons/light/alert-outline.png";
				snack.getStyleClass().add("warning");
			}else if(type.equals("info")) {
				iconPath = "/application/assets/icons/light/information-outline.png";
				snack.getStyleClass().add("info");
			}
			
			Image icon = new Image(iconPath);
			snackIcon.setImage(icon);
			snackTitle.setText(title);
			snackMessage.setText(message);
		
			new Thread(() -> {
				try {
					snack.setVisible(true);
					Thread.sleep(new Long(milliseconds));
					snack.setVisible(false);
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}).start();
		}catch( Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
