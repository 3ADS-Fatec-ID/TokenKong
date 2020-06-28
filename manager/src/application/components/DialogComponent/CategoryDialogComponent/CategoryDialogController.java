package application.components.DialogComponent.CategoryDialogComponent;

import java.util.ArrayList;

import application.DAO.CategoryDAO;
import application.components.AlertComponent.Alert;
import application.components.ConfirmComponent.Confirm;
import application.components.ConfirmComponent.Confirm.ConfirmCallback;
import application.models.Category;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CategoryDialogController {
	
	ArrayList<Category> categories = new ArrayList<Category>();
	
	@FXML public ImageView closeDialog;
	@FXML public TextField categoryName;
	@FXML public Button add_category;
	@FXML public VBox content;
	
	@FXML public void initialize() {
		loadCategories();
		add_category.setOnAction(submit());
	}
	
	private void loadCategories() {
		content.getChildren().clear();
		ArrayList<Category> categories = new ArrayList<Category>();
		try {
			categories = CategoryDAO.getAll();
			for(Category category: categories){
				Node categoryListItem = createCategoryListItem(category);
				content.getChildren().add(categoryListItem);			
			}
		}catch( Exception e ) {
			System.out.println(e.getMessage());
			Alert.showAlert(closeDialog.getScene(), "Error", "Could not list categories", "error", 5000);
		}
	}
	
	private Node createCategoryListItem( Category category ){
		Node node = new HBox();
		
		try {
			CategoryListItemController categoryListItemController = new CategoryListItemController();
			categoryListItemController.setCategory(category);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/components/DialogComponent/CategoryDialogComponent/CategoryListItem.fxml"));
			fxmlLoader.setController(categoryListItemController);
			node = fxmlLoader.load();
			
			categoryListItemController.removeButton.setOnMouseClicked(removeCategory(category));
			
			categories.add(category);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return node;
	}
	
	private EventHandler<MouseEvent> removeCategory(Category category){
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent event) {
				
				Scene scene = ((Node)event.getSource()).getScene();
				
				ConfirmCallback callback = new ConfirmCallback() {
					public EventHandler<ActionEvent>confirm(){
						EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
							@Override
							public void handle( ActionEvent event) {
								try {
									Confirm.close(scene);
									CategoryDAO.delete(category.id);
									loadCategories();
									categoryName.clear();
									Alert.showAlert(closeDialog.getScene(), "Success", "Category was successfully deleted", "success", 5000);
								}catch(Exception e) {
									System.out.println(e.getMessage());
									Alert.showAlert(closeDialog.getScene(), "Error", "Unable to delete category", "error", 5000);
								}
							}
						};
						return eventHandler;
					}
				};
				
				Confirm.show(scene, "Warning", "Are you sure you really want to delete this category?", "warning", true, callback);
			}
		};
		
		return eventHandler;
	}
	
	private EventHandler<ActionEvent> submit(){
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				if(!categoryName.getText().equals("")) {
					Category category = new Category(null, categoryName.getText());
					try {
						category = CategoryDAO.insert(category);
						loadCategories();
						categoryName.clear();
						Alert.showAlert(closeDialog.getScene(), "Success", "Category was successfully created", "success", 5000);
					}catch(Exception e) {
						System.out.println(e.getMessage());
						Alert.showAlert(closeDialog.getScene(), "Error", "Couldn't create a new category", "error", 5000);
					}
				}else {
					Alert.showAlert(closeDialog.getScene(), "There's nothing to save!", "Please enter the category name.", "warning", 5000);
				}
			}
		};
		
		return eventHandler;
	}
}
