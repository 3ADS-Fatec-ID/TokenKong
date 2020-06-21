package application.screens.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Main;
import application.DAO.DB;
import application.models.User;
import application.models.User.Role;
import application.models.User.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class UserFormController {
	private DB database = new DB();
	private User user = new User();
	
	ArrayList<Status> userStatuses = new ArrayList<Status>();
	ArrayList<Role> userRoles = new ArrayList<Role>();
	
	@FXML 
	public Button go_back;
	@FXML
	public ImageView breadcrumb_icon;
	@FXML
	public ImageView title_icon;
	@FXML 
	public Label breadcrumb_title;
	@FXML 
	public Label title;
	@FXML 
	public HBox page_content;
	@FXML 
	public Button cancel_button;
	@FXML 
	public Button submit_button;
	@FXML 
	public TextField user_name;
	@FXML 
	public TextField user_email;
	@FXML 
	public TextField user_password;
	@FXML 
	public TextField user_confirm;
	@FXML
	public ChoiceBox<String> user_role;
	@FXML
	public ChoiceBox<String> user_status;
	
	public void initialize() {
		this.loadUserStatuses();
		this.loadUserRoles();
		
		this.setInitialFormValues();
		
		this.go_back.setOnMouseClicked(this.goBack());
		this.cancel_button.setOnMouseClicked(goBack());
		this.submit_button.setOnAction(submit());
	}
	
	private void loadUserStatuses() {
		database.connect();
		PreparedStatement pstmtSelect = null;
		ResultSet resultSet = null;
		
		if(database.connection != null) {
			try {
				
				String querySelect = "SELECT * FROM user_status";
				
				pstmtSelect = database.connection.prepareStatement(querySelect);
				
				if (pstmtSelect.execute()) {
					
					resultSet = pstmtSelect.getResultSet();
					if(resultSet != null) {
						userStatuses.clear();
						while (resultSet.next()) {
							Status userStatus = new Status(resultSet.getInt("id"), resultSet.getString("name"));
							userStatuses.add(userStatus);
						}
					}
				}else {
					//Mostrar mensagem de erro
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(pstmtSelect != null && !pstmtSelect.isClosed()) {
						pstmtSelect.close();
					}
					database.disconnect();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadUserRoles() {
		database.connect();
		PreparedStatement pstmtSelect = null;
		ResultSet resultSet = null;
		
		if(database.connection != null) {
			try {
				
				String querySelect = "SELECT * FROM user_role";
				
				pstmtSelect = database.connection.prepareStatement(querySelect);
				
				if (pstmtSelect.execute()) {
					
					resultSet = pstmtSelect.getResultSet();
					if(resultSet != null) {
						userRoles.clear();
						while (resultSet.next()) {
							Role userRole = new Role(resultSet.getInt("id"), resultSet.getString("name"));
							userRoles.add(userRole);
						}
					}
				}else {
					//Mostrar mensagem de erro
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(pstmtSelect != null && !pstmtSelect.isClosed()) {
						pstmtSelect.close();
					}
					database.disconnect();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setInitialFormValues() {
		
		ObservableList<String> userStatuses = FXCollections.observableArrayList();
		ObservableList<String> userRoles = FXCollections.observableArrayList();
		
		for(Status userStatus : this.userStatuses) {
			userStatuses.add(userStatus.name);
		}
		for(Role userRole : this.userRoles) {
			userRoles.add(userRole.name);
		}
		
		user_status.setItems(userStatuses);
		user_role.setItems(userRoles);
		
		if(this.user.getId() != null) {
			breadcrumb_icon.setImage(new Image("application/assets/icons/light/pencil-outline.png"));
			title_icon.setImage(new Image("application/assets/icons/light/pencil-outline.png"));
			breadcrumb_title.setText("Edit");
			title.setText("Edit User");
			
			user_name.setText(user.getName());
			user_email.setText(user.getEmail());
			user_password.setText(user.getPassword());
			user_confirm.setText(user.getPassword());
			user_role.setValue(user.getRole().name);
			user_status.setValue(user.getStatus().name);
		}else {
			breadcrumb_icon.setImage(new Image("application/assets/icons/light/plus-thick.png"));
			title_icon.setImage(new Image("application/assets/icons/light/plus-thick.png"));
			breadcrumb_title.setText("New");
			title.setText("New User");
			
			user_role.getSelectionModel().selectFirst();
			user_status.getSelectionModel().selectFirst();
		}
	}
	
	private EventHandler<ActionEvent> submit(){
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				if(user.getId() == null) {
					createUser();
				}else {
					updateUser();
				}
			}
		};
		
		return eventHandler;
	}
	
	private void createUser() {
		database.connect();
		
		PreparedStatement pstmtInsert = null;
		
		if(database.connection != null) {
			try {
				Integer statusId = null;
				Integer roleId = null;
				
				for(Status userStatus : userStatuses) {
					if(userStatus.name == user_status.getValue()) {
						statusId = userStatus.id;
					}
				}
				for(Role userRole : userRoles) {
					if(userRole.name == user_role.getValue()) {
						roleId = userRole.id;
					}
				}
				
				if(canSave() && statusId != null && roleId != null) {
					String queryUpdate = "INSERT INTO user (name, email, password, role_id, status_id) VALUES (?, ?, ?, ?, ?)";
					
					pstmtInsert = database.connection.prepareStatement(queryUpdate);
					
					pstmtInsert.setString	(1, user_name.getText());
					pstmtInsert.setString	(2, user_email.getText());
					pstmtInsert.setString	(3, user_password.getText());
					pstmtInsert.setInt		(4, roleId);
					pstmtInsert.setInt		(5, statusId);

					if(pstmtInsert.execute()) {
						database.connection.commit();
					}else {
						//Mostrar mensagem de erro
					}
				}else {
					//show required fields
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(pstmtInsert != null && !pstmtInsert.isClosed()) {
						pstmtInsert.close();
					}
					database.disconnect();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	private void updateUser() {
		
	}
	
	public boolean canSave() {
		return ( 
			!user_name.getText().isEmpty() 		&&
			!user_email.getText().isEmpty() 	&&
			!user_confirm.getText().isEmpty() 	&&
			!user_password.getText().isEmpty()
		);
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/screens/UsersView.fxml"));
					HBox.setHgrow(page, Priority.ALWAYS);
					
					if(pagesParent.getChildren().isEmpty()) {
						pagesParent.getChildren().add(page);
					}else {
						pagesParent.getChildren().set(0, page);			
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};
		
		return eventHandler;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
