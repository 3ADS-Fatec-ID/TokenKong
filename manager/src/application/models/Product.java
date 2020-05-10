package application.models;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Product {
	
	private Integer id = null;
	private Double price = null;
	private String name = null;
	private String description = null;
	private ArrayList<ProductImage> images = new ArrayList<ProductImage>();
	
	public Product(){
		this.images.add(0, new ProductImage("/application/assets/images/products/no_image.png"));
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<ProductImage> getImages() {
		return images;
	}
	public void setImages(ArrayList<ProductImage> images) {
		this.images = images;
	}
	public Image getCoverImage() {
		return images.get(0);
	}
	public void setCoverImage(ProductImage image) {
		this.images.add(0, image);
	}
}
