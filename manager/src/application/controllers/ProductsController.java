package application.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class ProductsController {
	@FXML
	public ScrollPane scroller;
	@FXML
	public FlowPane products_grid;

	@FXML
	public void initialize() {
		scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
            	products_grid.setPrefWidth(bounds.getWidth());
            }
        });
		//Aqui vai a query que busca os produtos e carrega um por um
		this.loadProducts("lalala", 10.00, "clock1_1.jpg");
	}
	
	public void loadProducts(String name, Double price, String image) {
		try {
			
			ProductCardController productCardController = new ProductCardController();
			
			productCardController.setName(name);
			productCardController.setPrice(price);
			productCardController.setImage(image);
			
			StackPane stackPane = new StackPane();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/components/ProductCardComponent.fxml"));
			fxmlLoader.setController(productCardController);
			stackPane = fxmlLoader.load();
			
			products_grid.getChildren().add(stackPane);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
