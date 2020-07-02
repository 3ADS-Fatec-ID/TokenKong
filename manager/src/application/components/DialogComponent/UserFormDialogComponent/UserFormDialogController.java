package application.components.DialogComponent.UserFormDialogComponent;

import java.util.ArrayList;

import application.DAO.RoleDAO;
import application.DAO.StatusDAO;
import application.DAO.UserDAO;
import application.components.AlertComponent.Alert;
import application.components.ConfirmComponent.Confirm;
import application.components.ConfirmComponent.Confirm.ConfirmCallback;
import application.components.DialogComponent.Dialog;
import application.models.User;
import application.models.User.Role;
import application.models.User.Status;
import application.screens.controllers.UsersController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UserFormDialogController {
	
	boolean editing = false;
	
	public UsersController usersController;
	ArrayList<Status> userStatuses = new ArrayList<Status>();
	ArrayList<Role> userRoles = new ArrayList<Role>();
	private User user = new User();
	
	@FXML public ImageView closeDialog;
	@FXML public Label headerTitle;
	@FXML public ImageView headerIcon;
	@FXML public Button cancelButton;
	@FXML public Button submitButton;
	@FXML public Button deleteButton;
	@FXML public TextField userName;
	@FXML public TextField userEmail;
	@FXML public PasswordField userPassword;
	@FXML public PasswordField userConfirm;
	@FXML public ChoiceBox<String> userRole;
	@FXML public ChoiceBox<String> userStatus;
	
	public void initialize() {
		initChoiceBoxes();
		setButtonsActions();
		setInitialFormValues();
	}
	
	private void setInitialFormValues() {
		editing = (this.user.getId() != null);
		if(editing) {
			User iUser;
			try {
				headerTitle.setText("Edit User");
				
				iUser = UserDAO.getLocalUserData();
				if(iUser != null && iUser.getId() != this.user.getId()) {
					userPassword.setDisable(true);
					userConfirm.setDisable(true);
				}else {
					userStatus.setDisable(true);
					userRole.setDisable(true);
				}
				userRole.setValue(user.getRole().name);
				userStatus.setValue(user.getStatus().name);
				
				userName.setText(user.getName());
				userEmail.setText(user.getEmail());
				userPassword.setText(user.getPassword());
				userConfirm.setText(user.getPassword());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			headerTitle.setText("New User");
		}
		deleteButton.setVisible(editing);
	}
	
	private void setButtonsActions() {
		closeDialog.setId("closeDialog");
		cancelButton.setId("closeDialog");
		closeDialog.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event){
				close(event);
			}
		});
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				if(editing)
					deleteUser(event);
			}
		});
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				close(event);
			}
		});
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				submit(event);
			}
		});
	}
	
	private void initChoiceBoxes() {
		userRole.setMaxWidth(Double.MAX_VALUE);
		userStatus.setMaxWidth(Double.MAX_VALUE);
		
		loadRoles();
		loadStatus();
		ObservableList<String> userRoles = FXCollections.observableArrayList();
		ObservableList<String> userStatuses = FXCollections.observableArrayList();
		
		for(Role userRole : this.userRoles) {
			userRoles.add(userRole.name);
		}		
		for(Status userStatus : this.userStatuses) {
			userStatuses.add(userStatus.name);
		}
		
		userRole.setItems(userRoles);
		userStatus.setItems(userStatuses);
	}
	
	private void loadRoles() {
		try {
			userRoles = RoleDAO.getAll();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void loadStatus() {
		try {
			userStatuses = StatusDAO.getAll();			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void deleteUser(Event event) {
		Scene scene = ((Node)event.getSource()).getScene();
		
		ConfirmCallback callback = new ConfirmCallback() {
			public EventHandler<ActionEvent>confirm(){
				EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
					@Override
					public void handle( ActionEvent event) {
						
						try {
							Confirm.close(scene);
							UserDAO.delete(user.getId());
							reloadUserList();
							Dialog.close(scene, closeDialog);
							Alert.showAlert(scene, "Success", "User was successfully deleted!", "success", 5000);
						}catch(Exception e) {
							System.out.println(e.getMessage());
							Alert.showAlert(scene, "An error has occurred", "Couldn't delete user", "error", 5000);
						}
					}
				};
				return eventHandler;
			}
		};
		
		Confirm.show(scene, "Warning", "Are you shure that you realy want to delete this user?", "warning", true, callback);
	}
	
	private void submit(Event event){
		if(editing) {
			updateUser(event);			
		}else {
			createUser(event);
		}
	}
	
	private void updateUser(Event event) {
		Scene scene = ((Node)event.getSource()).getScene();
		
		try {
			Role chosenRole = getChosenRole();
			Status chosenStatus = getChosenStatus();
			
			User user = new User();
			user.setId(this.user.getId());
			user.setEmail(userEmail.getText());
			user.setName(userName.getText());
			if(chosenRole != null)
				user.setRole(chosenRole);
			if(chosenStatus != null)
				user.setStatus(chosenStatus);
			user.setPassword(userPassword.getText());
		
			this.user = UserDAO.update(user);
			reloadUserList();
			
			if(!this.user.equals(null)) {
				setInitialFormValues();
				Alert.showAlert(scene, "Success", "User was successfully updated!", "success", 5000);
			}else {
				Alert.showAlert(scene, "Success", "User was successfully updated!", "success", 5000);
				Dialog.close(scene, closeDialog);
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			Alert.showAlert(scene, "An error has occurred", "Couldn't save product", "error", 5000);
		}
	}
	
	private void createUser(Event event) {
		Scene scene = ((Node)event.getSource()).getScene();
		
		try {
			Role chosenRole = getChosenRole();
			Status chosenStatus = getChosenStatus();
			
			User user = new User();
			if(userPassword.getText().equals(userConfirm.getText())) {
				user.setPassword(userPassword.getText());
				user.setName(userName.getText());
				user.setEmail(userEmail.getText());
				if(chosenRole != null)
					user.setRole(chosenRole);
				if(chosenStatus != null)
					user.setStatus(chosenStatus);
			
				this.user = UserDAO.create(user);
				reloadUserList();
				
				if(!this.user.equals(null)) {
					setInitialFormValues();
					Alert.showAlert(scene, "Success", "User was successfully created!", "success", 5000);
				}else {
					Alert.showAlert(scene, "Success", "User was successfully created!", "success", 5000);
					Dialog.close(scene, closeDialog);
				}
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			Alert.showAlert(scene, "An error has occurred", "Could not create product", "error", 5000);
		}
	}
	
	private Role getChosenRole() {
		Role chosenRole = null;
		String roleName = userRole.getValue();
		if(roleName != null) {
			for(Role role : userRoles) {
				if(role.name == userRole.getValue()) {
					chosenRole = role;
					break;
				}
			}
		}
		return chosenRole;
	}
	
	private Status getChosenStatus() {
		Status chosenStatus = null;
		String statusName = userStatus.getValue();
		if(statusName != null) {
			for(Status status : userStatuses) {
				if(status.name == userStatus.getValue()) {
					chosenStatus = status;
					break;
				}
			}
		}
		return chosenStatus;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	private void reloadUserList() {
		this.usersController.loadUsers();
	}
	
	private void close(Event event){
		Node target = (Node)event.getTarget();
		Scene scene = ((Node)event.getSource()).getScene();
		Dialog.close(scene, target);
	}
}
