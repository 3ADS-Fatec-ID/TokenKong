package application.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ProductImage extends Image{
	
	public ProductImage(String string) {
		super(string);
	}
	
	private Integer id;
	private Integer imageId;
	private Integer productId;
	private String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getImageId() {
		return imageId;
	}
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String save() {
		String ext = this.getName().split("\\.")[1];
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String imageName = UtilMethods.generateRandomString(6) + "_" + timestamp.getTime();
		
		File outputFile = new File("./../assets/images/products/"+imageName);
	    BufferedImage bImage = SwingFXUtils.fromFXImage(this, null);
	    
		try {
		    ImageIO.write(bImage, ext, outputFile);
		} catch (IOException e) {
		   throw new RuntimeException(e);
		}
		return imageName + "." + ext;
	}
	
}
