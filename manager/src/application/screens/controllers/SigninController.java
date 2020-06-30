package application.screens.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.DAO.UserDAO;
import application.components.AlertComponent.Alert;
import application.components.ConfirmComponent.Confirm;
import application.components.DialogComponent.Dialog;
import application.components.DrawerComponent.DrawerController;
import application.models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SigninController implements Initializable{
	
	public double xOffset = 0;
	public double yOffset = 0;
    
	@FXML public ImageView close_button;
	@FXML public TextField userName_textField;
	@FXML public PasswordField password_passwordField;
	@FXML public CheckBox rememberMe_checkBox;
	@FXML public Button signin_button;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		signin_button.setOnAction(signin());
		close_button.setOnMouseClicked(close());
	}
	
	public EventHandler<MouseEvent> close() {
		
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) ((ImageView)event.getSource()).getScene().getWindow();
				stage.close();
			}
		};
		
		return eventHandler;
		
	}
	
	public EventHandler<ActionEvent> signin() {
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				String userName = userName_textField.getText();
				String password = password_passwordField.getText();
				
				try {
					User user = UserDAO.getUserByEmailAndPassword(userName, password);
					if(user != null) {
						UserDAO.saveLocalUserData(user);
					    openApp();
					}else {
						System.out.println("Not authenticated!");
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};
		
		return eventHandler;
		
	}
	
	public void openApp() {
		try {
			
			Stage rootStage = (Stage) signin_button.getScene().getWindow();
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/components/DrawerComponent/DrawerComponent.fxml"));
			DrawerController drawerController = new DrawerController();
			fxmlLoader.setController(drawerController);
			Parent drawer = fxmlLoader.load();
			
			Dialog.initializeDialog(drawerController.composition);
			Alert.initializeAlert(drawerController.composition);
			Confirm.initializeConfirm(drawerController.composition);
			
			Scene scene = new Scene(drawer);
			
			rootStage.close();
			rootStage = new Stage();
			rootStage.setScene(scene);
			rootStage.show();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
