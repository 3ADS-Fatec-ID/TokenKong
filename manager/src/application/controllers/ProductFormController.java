package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Main;
import application.models.DB;
import application.models.Product;
import application.models.ProductImage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ProductFormController{
	
	DB database = new DB();
	
	private Product product = new Product();
	final FileChooser fileChooser = new FileChooser();
	ImagePickerController imagePickerController = new ImagePickerController();
	
	@FXML public HBox page_content;
	@FXML public Button go_back;
	@FXML public TextField product_name;
	@FXML public TextField product_price;
	@FXML public TextArea product_description;
	
	public void initialize() {
		this.setInitialFormValues();
		this.generateImagePicker();
		this.setGoBackButtonAction();
	}
	
	private void setInitialFormValues() {
		product_name.setText(product.getName());
		product_price.setText(String.format("R$ %.2f", product.getPrice()));
		product_description.setText(product.getDescription());
	}
	
	private void loadProduct() {
		if(product.getId() != null) {
			database.connect();
			ResultSet resultSet = null;
			
			String query = "";
			query += "SELECT ";
			query += "P.*, ";
			query += "I.name as image, ";
			query += "I.id as image_id, ";
			query += "PI.id as product_image_id ";
			query += "FROM product as P ";
			query += "LEFT JOIN product_image as PI ";
			query += "ON PI.product_id = P.id ";
			query += "LEFT JOIN image as I ";
			query += "ON I.id = PI.image_id ";
			query += "WHERE P.id = " + product.getId() + " ";
			
			if(this.database.connection != null) {
				try{				
					PreparedStatement preparedStatement = this.database.connection.prepareStatement(query);
					if (preparedStatement.execute()) {
						
						resultSet = preparedStatement.getResultSet();
						if(resultSet != null) {
							ArrayList<ProductImage> productImages = new ArrayList<ProductImage>();
							while (resultSet.next()) {
								ProductImage productImage = new ProductImage("/application/assets/images/products/"+resultSet.getString("image"));
								productImage.setName(resultSet.getString("image"));
								productImage.setImageId(resultSet.getInt("image_id"));
								productImage.setProductId(resultSet.getInt("id"));
								productImage.setId(resultSet.getInt("product_image_id"));
								
								productImages.add(productImage);
							}
							product.setImages(productImages);
							this.imagePickerController.setImages(product.getImages());
						}
					}
				}catch(SQLException e) {
					System.out.println(e.getMessage());
				}finally {
					this.database.disconnect();
				}	
			}
		}
	}
	
	private void setGoBackButtonAction() {
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
	
	private void generateImagePicker() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/components/ImagePicker.fxml"));
			ArrayList<ProductImage> images = product.getImages();
			imagePickerController.setImages(images);
			
			fxmlLoader.setController(imagePickerController);
			VBox imagePicker = fxmlLoader.load();
			HBox.setHgrow(imagePicker, Priority.ALWAYS);
			VBox.setMargin(imagePicker, new Insets(0, 0, 10, 0));
			((VBox) page_content.getChildren().get(0)).getChildren().set(1,imagePicker);
			
			this.loadProduct();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}

}
