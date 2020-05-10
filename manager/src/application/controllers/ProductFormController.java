package application.controllers;

import java.io.File;

import application.Main;
import application.models.Product;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ProductFormController{
	
	private Product product = new Product();
	
	final FileChooser fileChooser = new FileChooser();
	
	@FXML public VBox product_form;
	@FXML public Button go_back;
	@FXML public Button chooseImage_button;
	@FXML public ImageView product_image;
	@FXML
	public void initialize() {
		
		product_image.setImage(product.getCoverImage());
		
		this.chooseImage_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Node source = (Node)event.getSource();
				Stage stage = (Stage)source.getScene().getWindow();
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					Image image = new Image(file.toURI().toString());
					product.setCoverImage(image);
					product_image.setImage(product.getCoverImage());
				}				
			}
        });
		
		this.go_back.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/views/screens/ProductsView.fxml"));
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
		});
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}

}
