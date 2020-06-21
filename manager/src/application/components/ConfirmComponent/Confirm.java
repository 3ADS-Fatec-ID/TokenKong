package application.components.ConfirmComponent;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Confirm {
	
	public static void initializeConfirm(StackPane composition) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader();
			ConfirmController confirmController = new ConfirmController();
			loader.setController(confirmController);
			File file = new File("src\\application\\components\\ConfirmComponent\\Confirm.fxml");
			loader.setLocation(file.toURI().toURL());
			VBox confirm = (VBox)loader.load();

			composition.getChildren().add(4, confirm);
			StackPane.setAlignment(confirm, Pos.CENTER);
			confirm.setVisible(false);
			
		}catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}

	public static void show (Scene scene, String title, String message, String type, Boolean cancelable, ConfirmCallback callback ) {
		try {
			StackPane composition = (StackPane)scene.getRoot();
			VBox confirm = (VBox)composition.getChildren().get(4);
			
			ImageView confirmIcon = (ImageView)((HBox)confirm.getChildren().get(0)).getChildren().get(0);
			Label confirmTitle = (Label)((VBox)((HBox)confirm.getChildren().get(0)).getChildren().get(1)).getChildren().get(0);
			Label confirmMessage = (Label)((VBox)((HBox)confirm.getChildren().get(0)).getChildren().get(1)).getChildren().get(1);
			
			Button cancelButton = (Button)((HBox)confirm.getChildren().get(1)).getChildren().get(0);
			Button confirmButton = (Button)((HBox)confirm.getChildren().get(1)).getChildren().get(1);
			confirmButton.setOnAction( callback.confirm() );
			cancelButton.setOnAction( new EventHandler<ActionEvent>() {
				@Override 
				public void handle(ActionEvent event) {
					close(((Node)event.getSource()).getScene());
				}
			});
			
			String iconPath = "";
			confirm.getStyleClass().clear();
			if(type.equals("success")) {
				iconPath = "/application/assets/icons/light/check-bold.png";
				confirm.getStyleClass().add("success");
				confirmButton.getStyleClass().add("text_success");
			}else if(type.equals("error")) {
				iconPath = "/application/assets/icons/light/close-thick.png";
				confirm.getStyleClass().add("error");
				confirmButton.getStyleClass().add("text_error");
			}else if(type.equals("warning")) {
				iconPath = "/application/assets/icons/light/alert-outline.png";
				confirm.getStyleClass().add("warning");
				confirmButton.getStyleClass().add("text_warning");
			}else if(type.equals("info")) {
				iconPath = "/application/assets/icons/light/information-outline.png";
				confirm.getStyleClass().add("info");
				confirmButton.getStyleClass().add("text_info");
			}
			
			Image icon = new Image(iconPath);
			confirmIcon.setImage(icon);
			confirmTitle.setText(title);
			confirmMessage.setText(message);
			cancelButton.setVisible(cancelable);
			
			confirm.setVisible(true);					

		}catch( Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void close(Scene scene){
		StackPane composition = (StackPane)scene.getRoot();
		VBox confirm = (VBox)composition.getChildren().get(4);
		confirm.setVisible(false);
	}
	
	public interface ConfirmCallback{
        public EventHandler<ActionEvent>confirm();
    }
}
