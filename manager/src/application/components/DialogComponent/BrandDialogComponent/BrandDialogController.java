package application.components.DialogComponent.BrandDialogComponent;

import java.util.ArrayList;

import application.components.AlertComponent.*;
import application.DAO.BrandDAO;
import application.models.Brand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BrandDialogController {
	
	ArrayList<Brand> brands = new ArrayList<Brand>();
	
	@FXML public ImageView closeDialog;
	@FXML public TextField brandName;
	@FXML public Button add_brand;
	@FXML public VBox content;
	
	@FXML
	public void initialize() {
		loadBrands();
		
		add_brand.setOnAction(submit());
	}
	
	private void loadBrands() {
		content.getChildren().clear();
		ArrayList<Brand> brands = new ArrayList<Brand>();
		try {
			brands = BrandDAO.getAll();
			for(Brand brand: brands){
				Node brandListItem = createBrandListItem(brand);
				content.getChildren().add(brandListItem);			
			}
		}catch( Exception e ) {
			System.out.println(e.getMessage());
			Alert.showAlert(closeDialog.getScene(), "Error", "Was not possible to list the brands", "error", 5000);
		}
	}
	
	private Node createBrandListItem( Brand brand ){
		Node node = new HBox();
		
		try {
			BrandListItemController brandListItemController = new BrandListItemController();
			brandListItemController.setBrand(brand);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/components/DialogComponent/BrandDialogComponent/BrandListItem.fxml"));
			fxmlLoader.setController(brandListItemController);
			node = fxmlLoader.load();
			
			brandListItemController.removeButton.setOnMouseClicked(removeBrand(brand));
			
			brands.add(brand);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return node;
	}
	
	private EventHandler<MouseEvent> removeBrand(Brand brand){
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent event) {
				try {
					BrandDAO.delete(brand.id);
					loadBrands();
					brandName.clear();
					Alert.showAlert(closeDialog.getScene(), "Success", "Brand deleted with success", "success", 5000);
				}catch(Exception e) {
					System.out.println(e.getMessage());
					Alert.showAlert(closeDialog.getScene(), "Error", "Was not possible to delete the brand", "error", 5000);
				}
			}
		};
		
		return eventHandler;
	}
	
	private EventHandler<ActionEvent> submit(){
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				if(!brandName.getText().equals("")) {
					Brand brand = new Brand(null, brandName.getText());
					try {
						brand = BrandDAO.insert(brand);
						loadBrands();
						brandName.clear();
						Alert.showAlert(closeDialog.getScene(), "Success", "Brand created with success", "success", 5000);
					}catch(Exception e) {
						System.out.println(e.getMessage());
						Alert.showAlert(closeDialog.getScene(), "Error", "Was not possible to create a new brand", "error", 5000);
					}
				}else {
					Alert.showAlert(closeDialog.getScene(), "There's nothing to save!", "Please, inform the name of the new brand.", "warning", 5000);
				}
			}
		};
		
		return eventHandler;
	}
	
}
