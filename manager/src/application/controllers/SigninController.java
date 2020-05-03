package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SigninController implements Initializable{
	
	@FXML public ImageView close_button;
	
	@FXML public TextField userName_textField;
	@FXML public PasswordField password_passwordField;
	@FXML public CheckBox rememberMe_checkBox;
	@FXML public Button signin_button;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML public void signin(ActionEvent event) {
		
		String userName = this.userName_textField.getText();
		String password = this.password_passwordField.getText();
		User user = new User(userName, password);
		
		if(user.isAuthenticated()) {
			user = null;
			
			try {
				Stage signin = (Stage) signin_button.getScene().getWindow();
				
				//open app window as a new stage
				StackPane drawer = FXMLLoader.load(getClass().getResource("/application/views/components/DrawerComponent.fxml"));
				Scene scene = new Scene(drawer);
				Stage app = new Stage();
				app.setScene(scene);
				app.show();
				
				//close signin stage/window
				signin.close();
				
			}catch(Exception e) {
				System.out.println(e);
			}
		}else {
			user = null;
			System.out.println("Not authenticated!");
		}
		
	}
	
	@FXML public void closeApplication(MouseEvent event) {
		Stage stage = (Stage) close_button.getScene().getWindow();
		stage.close();
	}
	
}
