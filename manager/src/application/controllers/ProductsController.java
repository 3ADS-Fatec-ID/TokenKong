package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Main;
import application.models.DB;
import application.models.Product;
import application.models.ProductImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProductsController {
	
	DB database = new DB();
	ArrayList<Product> products = new ArrayList<Product>();
	
	@FXML 
	public Button go_back;
	@FXML
	public ScrollPane scroller;
	@FXML
	public FlowPane products_grid;
	@FXML
	public VBox scroller_content;

	@FXML
	public void initialize() {
		
		this.go_back.setOnMouseClicked(this.goBack());
		
		scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
            	scroller_content.setPrefWidth(bounds.getWidth());
            	Parent parent = (Parent)scroller_content.getChildren().get(0);
            	parent.setStyle("-fx-background-color:transparent");
            	Parent parent2 = (Parent)parent.getChildrenUnmodifiable().get(0);
            	parent2.setStyle("-fx-background-color:transparent");
            }
        });
		
		this.loadProducts();
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
	
	private void loadProducts() {
		
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
		query += "GROUP BY P.id ";
		
		if(this.database.connection != null) {
			try{				
				PreparedStatement preparedStatement = this.database.connection.prepareStatement(query);
				if (preparedStatement.execute()) {
					
					resultSet = preparedStatement.getResultSet();
					if(resultSet != null) {
						
						while (resultSet.next()) {
							Node userCard = this.createCardComponent(resultSet);
							products_grid.getChildren().add(userCard);
						}
						
					}
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}finally {
				this.database.disconnect();
			}	
		}
		
	}
	
	private Node createCardComponent(ResultSet resultSet) {
			
		Node node = new StackPane();
		
		try {

			ProductCardController productCardController = new ProductCardController();
			Product product = new Product();
			
			product.setId(resultSet.getInt("id"));
			product.setName(resultSet.getString("name"));
			product.setPrice(resultSet.getDouble("price"));
			product.setQuantity(resultSet.getInt("quantity"));
			
			if(resultSet.getString("image") != null) {
				ProductImage image = new ProductImage("/application/assets/images/products/"+resultSet.getString("image"));
				image.setProductId(resultSet.getInt("id"));
				image.setImageId(resultSet.getInt("image_id"));
				image.setId(resultSet.getInt("product_image_id"));
				image.setName(resultSet.getString("image"));
				
				ArrayList<ProductImage> imageList = new ArrayList<ProductImage>();
				imageList.add(image);
				product.setImages(imageList);
			}
			
			productCardController.setProduct(product);
			this.products.add(product);
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/components/ProductCardComponent.fxml"));
			fxmlLoader.setController(productCardController);
			node = fxmlLoader.load();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return node;
	}

}
