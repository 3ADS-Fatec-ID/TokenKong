package application.screens.controllers;

import java.io.File;
import java.util.ArrayList;

import application.Main;
import application.DAO.ProductDAO;
import application.components.DialogComponent.Dialog;
import application.components.DialogComponent.BrandDialogComponent.BrandDialogController;
import application.components.DialogComponent.CategoryDialogComponent.CategoryDialogController;
import application.components.ProductCardComponent.ProductCardController;
import application.models.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProductsController{
	
	public BrandDialogController brandDialogController;
	public CategoryDialogController categoryDialogController;
	public ArrayList<Product> products = new ArrayList<Product>();
	
	@FXML public Button go_back;
	@FXML public ScrollPane scroller;
	@FXML public FlowPane products_grid;
	@FXML public VBox scroller_content;
	@FXML public Button brandButton;
	@FXML public Button categoryButton;

	@FXML
	public void initialize() {
		
		go_back.setOnMouseClicked( goBack() );
		brandButton.setOnAction( openBrandDialog() );
		categoryButton.setOnAction( openCategoryDialog() );
		scroller.viewportBoundsProperty().addListener( onScrollerDimensionsChange() );
		
		loadProducts();
	}
	
	private ChangeListener<Bounds> onScrollerDimensionsChange() {
		ChangeListener<Bounds> listener = new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
            	scroller_content.setPrefWidth(bounds.getWidth());
            	Parent parent = (Parent)scroller_content.getChildren().get(0);
            	parent.setStyle("-fx-background-color:transparent");
            	Parent parent2 = (Parent)parent.getChildrenUnmodifiable().get(0);
            	parent2.setStyle("-fx-background-color:transparent");
            }
        };
        
        return listener;
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/screens/HomeView.fxml"));
					HBox.setHgrow(page, Priority.ALWAYS);
					
					if(pagesParent.getChildren().isEmpty()) {
						pagesParent.getChildren().add(page);
					}else {
						pagesParent.getChildren().set(0, page);			
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());

					//show error message
					
				}
			}
		};
		
		return eventHandler;
	}
	
	private void loadProducts() {
		try {
			ArrayList<Product> products = ProductDAO.getAll();
			for(Product product: products){
				Node userCard = this.createCardComponent(product);
				products_grid.getChildren().add(userCard);			
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			
			//show error message
			
		}
	}
	
	private Node createCardComponent(Product product) {
			
		Node node = new StackPane();
		
		try {
			ProductCardController productCardController = new ProductCardController();
			productCardController.setProduct(product);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/components/ProductCardComponent/ProductCardComponent.fxml"));
			fxmlLoader.setController(productCardController);
			node = fxmlLoader.load();
			
			products.add(product);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return node;
	}
	
	private EventHandler<ActionEvent> openBrandDialog() {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() { 
			@Override 
			public void handle(ActionEvent event) {
				try {
										
					FXMLLoader loader = new FXMLLoader();
					brandDialogController = new BrandDialogController();
					loader.setController(brandDialogController);
					File file = new File("src\\application\\components\\DialogComponent\\BrandDialogComponent\\BrandDialog.fxml");
					loader.setLocation(file.toURI().toURL());
					VBox content = (VBox)loader.load();
					HBox.setHgrow(content, Priority.ALWAYS);
					
					brandDialogController.closeDialog.setId("closeDialog");
					brandDialogController.closeDialog.setOnMouseClicked(new EventHandler<Event>() { 
						@Override 
						public void handle(Event event) {
							Scene scene = (Scene)((Node)event.getSource()).getScene();
							Dialog.close(scene, (Node)event.getTarget());
						}
					});
					
					Scene scene = (Scene)((Node)event.getSource()).getScene();
					Dialog.show(scene, content);
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					
					//show alert load error;
					
				}
			}
		};
		
		return eventHandler;
	}
	
	private EventHandler<ActionEvent> openCategoryDialog() {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() { 
			@Override 
			public void handle(ActionEvent event) {
				try {
					FXMLLoader loader = new FXMLLoader();
					categoryDialogController = new CategoryDialogController();
					loader.setController(categoryDialogController);
					File file = new File("src\\application\\components\\DialogComponent\\CategoryDialogComponent\\CategoryDialog.fxml");
					loader.setLocation(file.toURI().toURL());
					VBox content = (VBox)loader.load();
					HBox.setHgrow(content, Priority.ALWAYS);
					
					categoryDialogController.closeDialog.setId("closeDialog");
					categoryDialogController.closeDialog.setOnMouseClicked(new EventHandler<Event>() { 
						@Override 
						public void handle(Event event) {
							Scene scene = (Scene)((Node)event.getSource()).getScene();
							Dialog.close(scene, (Node)event.getTarget());
						}
					});
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					
					//show alert load error;
					
				}
			}
		};
		
		return eventHandler;
	}

}
