package application.models;

import javafx.scene.control.ContextMenu;

public class ImageContextMenu extends ContextMenu{
	
	private ProductImage image = null;
	
	public ProductImage getImage() {
		return image;
	}

	public void setImage(ProductImage image) {
		this.image = image;
	}
}