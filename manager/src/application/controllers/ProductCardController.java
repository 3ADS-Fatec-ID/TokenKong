package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductCardController {
	
	String name = null;
	Double price = null;
	String imageName = null;
	
	@FXML
	public ImageView productImage;
	@FXML
	public Label productName;
	@FXML
	public Label productPrice;

	@FXML
	public void initialize() {
		this.productName.setText(this.name);
		this.productPrice.setText(String.format("R$ %.2f", this.price));
        Image image = new Image("/application/assets/images/products/"+this.imageName);
		this.productImage.setImage(image);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setImage(String imageName) {
		this.imageName = imageName;
	}
}
