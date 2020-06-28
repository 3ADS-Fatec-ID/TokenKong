package application.components.DialogComponent.CategoryDialogComponent;

import application.DAO.CategoryDAO;
import application.components.AlertComponent.Alert;
import application.models.Category;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class CategoryListItemController {
	
	private Category category = null;
	private boolean editing = false;
	
	@FXML
	public Label name;
	@FXML
	public ImageView editButton;
	@FXML 
	public ImageView removeButton;
	@FXML
	public ImageView cancelButton;
	@FXML
	public ImageView saveButton;
	@FXML
	public HBox categoryListItem;
	@FXML
	public TextField categoryTextField;
	
	@FXML
	public void initialize() {
		name.setText(category.name);
		((HBox)categoryListItem.getChildren().get(1)).setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				if(!editing) setEditMode();
			}
		});
		cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				unsetEditMode();
			}
		});
		saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				submit();
			}
		});
		editButton.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				setEditMode();
			}
		});
		
		setInitialState();
	}
	
	public void setInitialState() {
		HBox leftArea = (HBox)categoryListItem.getChildren().get(1);
		leftArea.getChildren().remove(1);
		categoryListItem.getChildren().remove(2);
		categoryListItem.getChildren().remove(2);
	}
	
	public void setEditMode() {
		HBox leftArea = (HBox)categoryListItem.getChildren().get(1);
		leftArea.getChildren().set(0, categoryTextField);
		categoryTextField.setText(category.name);
		categoryListItem.getChildren().add(saveButton);
		categoryListItem.getChildren().set(2, cancelButton);
		
		editing = true;
	}
	
	public void unsetEditMode() {
		HBox leftArea = (HBox)categoryListItem.getChildren().get(1);
		leftArea.getChildren().set(0, name);
		categoryListItem.getChildren().remove(2);
		categoryListItem.getChildren().remove(2);
		categoryListItem.getChildren().add(editButton);
		
		editing = false;
	}
	
	public void submit(){
		if(!categoryTextField.getText().equals("")) {
			if(!categoryTextField.getText().equals(this.category.name)) {
				Category category = new Category(this.category.id, categoryTextField.getText());
				try {
					this.category = CategoryDAO.update(category);
					name.setText(this.category.name);
					unsetEditMode();
					
					Alert.showAlert(categoryListItem.getScene(), "Success", "Category was successfully updated", "success", 5000);
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
					Alert.showAlert(categoryListItem.getScene(), "Error", "Could not update category", "error", 5000);
				}
			}else {
				Alert.showAlert(categoryListItem.getScene(), "Warning", "There was no update!", "warning", 5000);
			}
		}else {
			Alert.showAlert(categoryListItem.getScene(), "Warning", "There's nothing to save!", "warning", 5000);
		}
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
}
