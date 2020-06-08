package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import application.models.DB;
import application.models.User;
import application.models.UserRole;
import application.models.UserStatus;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class UsersController {
	
	private DB database = new DB();
	public ObservableList<User> users = FXCollections.observableArrayList();
	
	@FXML 
	public Button go_back;
	@FXML
	public Button add_user;
	@FXML
	public TableView<User> table;
	@FXML
    private TableColumn<User, Integer> column_code;
    @FXML
    private TableColumn<User, String> column_name;
    @FXML
    private TableColumn<User, String> column_email;
    @FXML
    private TableColumn<User, String> column_role;
    @FXML
    private TableColumn<User, String> column_status;

	@FXML
	public void initialize() {
		
		this.go_back.setOnMouseClicked(this.goBack());
		this.add_user.setOnAction(this.openUserForm());
		
		column_code.setCellValueFactory(new PropertyValueFactory<>("id"));
		column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		column_email.setCellValueFactory(new PropertyValueFactory<>("email"));
		column_role.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getRole().name));
		column_status.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getStatus().name));
		table.setItems(users);
		
		this.loadUsers();
	}
	
	private void loadUsers() {
		
		database.connect();
		ResultSet resultSet = null;
		
		String query = "";
		query += "SELECT ";
		query += "U.*, ";
		query += "UR.name as role_name, ";
		query += "US.name as status_name ";
		query += "FROM user as U ";
		query += "INNER JOIN user_status as US ";
		query += "ON U.status_id = US.id ";
		query += "INNER JOIN user_role as UR ";
		query += "ON U.role_id = UR.id ";
		query += "WHERE US.id != 4";
		
		if(this.database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				if (preparedStatement.execute()) {
					
					resultSet = preparedStatement.getResultSet();
					if(resultSet != null) {
						
						while (resultSet.next()) {
							User user = new User();
							user.setId(resultSet.getInt("id"));
							user.setName(resultSet.getString("name"));
							user.setEmail(resultSet.getString("email"));
							user.setRole( new UserRole(
								resultSet.getInt("role_id"),
								resultSet.getString("role_name")
							));
							user.setStatus( new UserStatus(
								resultSet.getInt("status_id"),
								resultSet.getString("status_name")
							));
							
							users.add(user);
						}
						
					}
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}finally {
				database.disconnect();
			}	
		}
		
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/views/screens/HomeView.fxml"));
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
	
	private EventHandler<ActionEvent> openUserForm(){
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/screens/UserFormView.fxml"));
					UserFormController userFormController = new UserFormController();
					fxmlLoader.setController(userFormController);
					VBox page = fxmlLoader.load();
					
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
	
}
