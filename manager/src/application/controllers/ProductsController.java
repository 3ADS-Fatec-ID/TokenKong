package application.controllers;

import java.util.ArrayList;

import application.Main;
import application.DAO.ProductDAO;
import application.models.Product;
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

	public ArrayList<Product> products = new ArrayList<Product>();
	
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
		
		ArrayList<Product> products = ProductDAO.getProducts();
		for(Product product: products){
			Node userCard = this.createCardComponent(product);
			products_grid.getChildren().add(userCard);			
		}
		
	}
	
	private Node createCardComponent(Product product) {
			
		Node node = new StackPane();
		
		try {
			ProductCardController productCardController = new ProductCardController();
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
