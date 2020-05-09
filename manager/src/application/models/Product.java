package application.models;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Product {
	
	private Integer id = null;
	private Double price = null;
	private String name = null;
	private ArrayList<Image> images = new ArrayList<Image>();
	
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
	public ArrayList<Image> getImages() {
		return images;
	}
	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}
	public Image getCoverImage() {
		return images.get(0);
	}
	public void setCoverImage(Image image) {
		this.images.add(0, image);
	}
}
