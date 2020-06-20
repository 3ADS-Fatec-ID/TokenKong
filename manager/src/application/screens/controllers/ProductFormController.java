package application.screens.controllers;

import java.util.ArrayList;

import application.Main;
import application.components.AlertComponent.*;
import application.components.ImagePickerComponent.ImagePickerController;
import application.DAO.ProductDAO;
import application.models.Product;
import application.models.ProductImage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ProductFormController{
	
	private Product product = new Product();
	final FileChooser fileChooser = new FileChooser();
	public ImagePickerController imagePickerController = new ImagePickerController();
	
	@FXML 
	public HBox page_content;
	@FXML 
	public Button go_back;
	@FXML 
	public Button cancel_button;
	@FXML 
	public Button submit_button;
	@FXML 
	public TextField product_name;
	@FXML 
	public TextField product_price;
	@FXML 
	public TextField product_quantity;
	@FXML 
	public TextArea product_description;
	
	public void initialize() {
		this.setInitialFormValues();
		this.generateImagePicker();
		
		this.go_back.setOnMouseClicked(this.goBack());
		this.cancel_button.setOnMouseClicked(goBack());
		this.submit_button.setOnAction(submit());
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	private void setInitialFormValues() {
		if(this.product.getId() != null) {
			product_name.setText(product.getName());
			product_price.setText(String.format("%.2f", product.getPrice()));
			product_description.setText(product.getDescription());
			product_quantity.setText(String.format("%d", product.getQuantity()));
		}
	}
	
	private void loadProduct() {
		Integer productId = product.getId();
		try {
			if(productId != null) {
				this.product = ProductDAO.getOne(productId);
				this.imagePickerController.setImages(product.getImages());
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void generateImagePicker() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/components/ImagePickerComponent/ImagePickerComponent.fxml"));
			ArrayList<ProductImage> images = product.getImages();
			imagePickerController.setImages(images);
			
			fxmlLoader.setController(imagePickerController);
			VBox imagePicker = fxmlLoader.load();
			HBox.setHgrow(imagePicker, Priority.ALWAYS);
			VBox.setMargin(imagePicker, new Insets(0, 0, 10, 0));
			((VBox) page_content.getChildren().get(0)).getChildren().set(2,imagePicker);
			
			this.loadProduct();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private EventHandler<ActionEvent> submit(){
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				updateProduct(event);
			}
		};
		
		return eventHandler;
	}
	
	private void updateProduct(ActionEvent event) {
		try {
			Product product = new Product();
			product.setId(this.product.getId());
			product.setName(product_name.getText());
			product.setImages(imagePickerController.getImages());
			product.setDescription(product_description.getText());
			product.setQuantity(Integer.parseInt(product_quantity.getText()));
			product.setPrice(Double.parseDouble(product_price.getText().replace(',', '.')));
		
			this.product = ProductDAO.update(product);
			
			if(!this.product.equals(null)) {
				
				setInitialFormValues();
				imagePickerController.setImages(this.product.getImages());
				Scene scene = ((Node)event.getSource()).getScene();
				Alert.showAlert(scene, "Success", "The product was updated with success!", "success", 5000);
				
			}else {
				//show success message with error to reload user
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			Scene scene = ((Node)event.getSource()).getScene();
			Alert.showAlert(scene, "An error has occurred", "Could not save product data", "error", 5000);
		}
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/screens/ProductsView.fxml"));
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
