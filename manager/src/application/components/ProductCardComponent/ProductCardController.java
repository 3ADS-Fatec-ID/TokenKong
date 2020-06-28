package application.components.ProductCardComponent;

import java.io.File;

import application.components.DialogComponent.Dialog;
import application.components.DialogComponent.ProductFormDialogComponent.ProductFormDialogController;
import application.screens.controllers.ProductsController;
import application.models.Product;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProductCardController {
	
	private Product product = new Product();
	public ProductsController productsController;
	public ProductFormDialogController productFormDialogController;
	
	@FXML public Pane card;
	@FXML public ImageView productImage;
	@FXML public Label productName;
	@FXML public Label productPrice;

	@FXML
	public void initialize() {
		
		this.productName.setText(this.product.getName());
		this.productPrice.setText(String.format("R$ %.2f", product.getPrice()));
		this.productImage.setImage(this.product.getCoverImage());
		card.setOnMouseClicked( openProductFormDialog() );
		/*card.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/screens/ProductFormView.fxml"));
					ProductFormController productFormController = new ProductFormController();
					productFormController.setProduct(product);
					fxmlLoader.setController(productFormController);
					VBox page = fxmlLoader.load();
					
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
		});*/
	}
	
	private EventHandler<MouseEvent> openProductFormDialog() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				try {
				
					FXMLLoader loader = new FXMLLoader();
					productFormDialogController = new ProductFormDialogController();
					productFormDialogController.productsController = productsController;
					productFormDialogController.setProduct(product);
					loader.setController(productFormDialogController);
					File file = new File("src\\application\\components\\DialogComponent\\ProductFormDialogComponent\\ProductFormDialog.fxml");
					loader.setLocation(file.toURI().toURL());
					VBox content = (VBox)loader.load();
					HBox.setHgrow(content, Priority.ALWAYS);
					
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
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
