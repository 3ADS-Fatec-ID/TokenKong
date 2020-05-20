package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Main;
import application.models.DB;
import application.models.Product;
import application.models.ProductImage;
import javafx.event.ActionEvent;
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
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public void initialize() {
		this.setInitialFormValues();
		this.generateImagePicker();
		
		this.go_back.setOnMouseClicked(this.goBack());
		this.cancel_button.setOnMouseClicked(goBack());
		this.submit_button.setOnAction(submit());
	}
	
	private void setInitialFormValues() {
		product_name.setText(product.getName());
		product_price.setText(String.format("%.2f", product.getPrice()));
		product_description.setText(product.getDescription());
		product_quantity.setText(String.format("%d", product.getQuantity()));
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
				}catch(SQLException e2) {
					System.out.println(e2.getMessage());
				}catch(Exception e1) {
					System.out.println(e1.getMessage());
				}finally {
					this.database.disconnect();
				}	
			}
		}
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
				updateProduct();
			}
		};
		
		return eventHandler;
	}
	
	private void updateProduct() {
		database.connect();
		
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtRemove = null;
		PreparedStatement pstmtInsert = null;
		
		if(database.connection != null) {
			try {				
				database.connection.setAutoCommit(false);
				
				//UPDATE PRODUCT SIMPLE DATA
				String queryUpdate = "UPDATE product SET name = ?, price = ?, quantity = ?, description = ? WHERE id = ?";
				
			    pstmtUpdate = database.connection.prepareStatement(queryUpdate);
				
				pstmtUpdate.setString	(1, product_name.getText());
				pstmtUpdate.setFloat	(2, Float.parseFloat(product_price.getText().replace(',', '.')));
				pstmtUpdate.setString	(3, product_quantity.getText());
				pstmtUpdate.setString	(4, product_description.getText());
				pstmtUpdate.setInt	(5, product.getId());
				
				pstmtUpdate.execute();
				
				ArrayList<Integer> imagesIdsToDelete = imagePickerController.getIdsToDelete();
				
				if(imagesIdsToDelete.size() > 0) {
					
					//DELETE PRODUCT LINK WITH IMAGES
					String queryRemove = "";
					queryRemove += "DELETE FROM ";
					queryRemove += "product_image WHERE ";
					queryRemove += "id IN ( ";
					for(int i = 0; i < imagesIdsToDelete.size(); i++) {
						if(i > 0) {
							queryRemove += ", ";				
						}
						queryRemove += "?";
					}
					queryRemove += " ) ";
					
					pstmtRemove = database.connection.prepareStatement(queryRemove);
					
					for(int i = 0; i < imagesIdsToDelete.size(); i++) {
						pstmtRemove.setInt( i + 1 , imagesIdsToDelete.get(i) );
					}
			        
					pstmtRemove.execute();
				}
				
				ArrayList<ProductImage> images = imagePickerController.getImages();
				if(!images.isEmpty()) {
					
					//INSERT IMAGES AND IMAGES PRODUCT LINK
					for(ProductImage image : images) {
						if(image.getImageId() == null) {
							image.save();
							String queryInsert = "";
							queryInsert += "INSERT INTO product_image ( image_id, product_id ) ";
							queryInsert += "VALUES ( ?, ? )";
							
							pstmtInsert = database.connection.prepareStatement(queryInsert);
							//salvar imagem na pasta e no banco -> retorna ids das imagens salvaas
							
							pstmtInsert.setInt( 1 , image.getImageId() );
							pstmtInsert.setInt( 2 , this.product.getId() );
							pstmtInsert.execute();
						}
					}
					
				}
				
				database.connection.commit();
				
			}catch(RuntimeException e){
				System.out.println(e.getMessage());
				try { 
					database.connection.rollback(); 
				} catch (SQLException e1) { 
					e1.printStackTrace(); 
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				try { 
					database.connection.rollback(); 
				} catch (SQLException e1) { 
					e1.printStackTrace(); 
				}
			}finally {
				try {
					if(pstmtUpdate != null && !pstmtUpdate.isClosed()) {
						pstmtUpdate.close();
					}
					if(pstmtRemove != null && !pstmtRemove.isClosed()) {
						pstmtRemove.close();
					}
					database.disconnect();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
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
		};
		
		return eventHandler;
	}

}
