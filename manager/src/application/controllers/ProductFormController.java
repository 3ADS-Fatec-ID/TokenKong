package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProductFormController implements Initializable{
	
	private Product product = null;
	HBox pagesParent = null;
	
	@FXML public VBox product_form;
	@FXML public Button go_back;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	public void setPagesParent(HBox pagesParent) {
		this.pagesParent = pagesParent;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@FXML public void goBack() {
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

}
