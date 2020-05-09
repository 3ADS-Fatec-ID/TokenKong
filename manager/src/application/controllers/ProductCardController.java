package application.controllers;

import application.models.Product;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProductCardController {
	
	private Product product;
	
	@FXML
	public Pane card;
	@FXML
	public ImageView productImage;
	@FXML
	public Label productName;
	@FXML
	public Label productPrice;

	@FXML
	public void initialize() {
		
		this.productName.setText(this.product.getName());
		this.productPrice.setText(String.format("R$ %.2f", this.product.getPrice()));
		this.productImage.setImage(this.product.getCoverImage());
		
		card.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				Node source = (Node)event.getSource();
				Parent parent = source.getParent();
				Parent parent2 = parent.getParent();
				Parent parent3 = parent2.getParent();
				Parent parent4 = parent3.getParent();
				Parent parent5 = parent4.getParent();
				Parent parent6 = parent5.getParent();
				HBox pagesParent = (HBox) parent6.getParent();
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/screens/ProductFormView.fxml"));
					ProductFormController productFormController = new ProductFormController();
					productFormController.setPagesParent(pagesParent);
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
		});
	}
	
	public void setProduct(Product  product) {
		this.product = product;
	}
}
