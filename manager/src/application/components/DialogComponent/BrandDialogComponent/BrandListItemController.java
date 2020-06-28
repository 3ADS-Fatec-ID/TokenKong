package application.components.DialogComponent.BrandDialogComponent;

import javafx.fxml.FXML;
import application.DAO.BrandDAO;
import application.models.Brand;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import application.components.AlertComponent.*;

public class BrandListItemController {
	
	private Brand brand = null;
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
	public HBox brandListItem;
	@FXML
	public TextField brandTextField;
	
	@FXML
	public void initialize() {
		name.setText(brand.name);
		((HBox)brandListItem.getChildren().get(1)).setOnMouseClicked(new EventHandler<MouseEvent>() { 
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
		HBox leftArea = (HBox)brandListItem.getChildren().get(1);
		leftArea.getChildren().remove(1);
		brandListItem.getChildren().remove(2);
		brandListItem.getChildren().remove(2);
	}
	
	public void setEditMode() {
		HBox leftArea = (HBox)brandListItem.getChildren().get(1);
		leftArea.getChildren().set(0, brandTextField);
		brandTextField.setText(brand.name);
		brandListItem.getChildren().add(saveButton);
		brandListItem.getChildren().set(2, cancelButton);
		
		editing = true;
	}
	
	public void unsetEditMode() {
		HBox leftArea = (HBox)brandListItem.getChildren().get(1);
		leftArea.getChildren().set(0, name);
		brandListItem.getChildren().remove(2);
		brandListItem.getChildren().remove(2);
		brandListItem.getChildren().add(editButton);
		
		editing = false;
	}
	
	public void submit(){
		if(!brandTextField.getText().equals("")) {
			if(!brandTextField.getText().equals(this.brand.name)) {
				Brand brand = new Brand(this.brand.id, brandTextField.getText());
				try {
					this.brand = BrandDAO.update(brand);
					name.setText(this.brand.name);
					unsetEditMode();
					
					Alert.showAlert(brandListItem.getScene(), "Success", "Brand was successfully updated", "success", 5000);
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
					Alert.showAlert(brandListItem.getScene(), "Error", "Could not update brand", "error", 5000);
				}
			}else {
				Alert.showAlert(brandListItem.getScene(), "Warning", "There was no update!", "warning", 5000);
			}
		}else {
			Alert.showAlert(brandListItem.getScene(), "Warning", "There's nothing to save!", "warning", 5000);
		}
	}
	
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
}
