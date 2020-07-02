package application.screens.controllers;

import java.io.File;
import java.util.ArrayList;

import application.Main;
import application.DAO.UserDAO;
import application.components.AlertComponent.Alert;
import application.components.DialogComponent.Dialog;
import application.components.DialogComponent.UserFormDialogComponent.UserFormDialogController;
import application.models.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class UsersController {
	
	public ObservableList<User> users = FXCollections.observableArrayList();
	public UserFormDialogController userFormDialogController;
	@FXML 
	public Button go_back;
	@FXML
	public Button userButton;
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
		
		go_back.setOnMouseClicked(this.goBack());
		userButton.setOnAction(this.openUserFormDialog(this, null));
		
		column_code.setCellValueFactory(new PropertyValueFactory<>("id"));
		column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		column_email.setCellValueFactory(new PropertyValueFactory<>("email"));
		column_role.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getRole().name));
		column_status.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getStatus().name));
		table.setItems(users);
		
		table.setRowFactory(tv -> {
		    TableRow<User> row = new TableRow<>();
		    UsersController usersController = this;
            
		    row.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent event) {
			    	if (! row.isEmpty() && event.getButton()==MouseButton.SECONDARY) {
			            User user = row.getItem();
			            
			            ContextMenu contextMenu = new ContextMenu();
						MenuItem item = new MenuItem("Editar");
						item.setOnAction(openUserFormDialog(usersController, user));
						item.setOnAction(openUserFormDialog(usersController, user));
						contextMenu.getItems().add(item);
						
			            contextMenu.show(row, event.getScreenX(), event.getScreenY());
			        }
			    }
			});
		    return row ;
		});
		
		this.loadUsers();
	}
	
	public void loadUsers() {
		Scene scene = userButton.getScene();
		this.users.clear();
		try {
			ArrayList<User> users = UserDAO.getAll();
			for(User user : users) {
				this.users.add(user);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			Alert.showAlert(scene, "Error", "Could not list products", "error", 5000);
		}
	}
	
	public void deleteUser(User user) {
		Scene scene = userButton.getScene();
		try {
			UserDAO.delete(user.getId());
			Alert.showAlert(scene, "Success", "User successfully deleted!", "error", 5000);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			Alert.showAlert(scene, "Error", "The product could not be deleted", "error", 5000);
		}
	}
	
	private EventHandler<ActionEvent> openUserFormDialog(UsersController usersController, User user) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() { 
			@Override 
			public void handle(ActionEvent event) {
				try {
										
					FXMLLoader loader = new FXMLLoader();
					userFormDialogController = new UserFormDialogController();
					userFormDialogController.usersController = usersController;
					if(user != null) userFormDialogController.setUser(user);
					loader.setController(userFormDialogController);
					File file = new File("src\\application\\components\\DialogComponent\\UserFormDialogComponent\\UserFormDialog.fxml");
					loader.setLocation(file.toURI().toURL());
					VBox content = (VBox)loader.load();
					HBox.setHgrow(content, Priority.ALWAYS);
					
					Scene scene = (Scene)table.getScene();
					Dialog.show(scene, content);
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					
					//show alert load error;
					
				}
			}
		};
		
		return eventHandler;
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/screens/HomeView.fxml"));
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
