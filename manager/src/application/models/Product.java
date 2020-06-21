package application.models;

import java.io.File;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import application.Utils;
import application.DAO.DB;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Product {
	
	private Integer id = null;
	private Double price = null;
	private Integer quantity = null;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
	
	public static class ProductImage extends BaseImage{
		
		private DB database = new DB();
		private Integer imageId;
		private Integer productId;
		
		public ProductImage(String string) {
			super(string);
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
		
		public void save() throws Exception {
			
			database.connect();
			
			String ext = this.getName().split("\\.")[1];
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String imageName = Utils.generateRandomString(6) + "_" + timestamp.getTime();
			this.setName(imageName+"."+ext);
			try {
				String path = Paths.get("").toAbsolutePath().toString();
				String fname= path+"/src/application/assets/images/products/"+this.getName();
				File file = new File(fname);
				
	            if (file != null && database.connection != null) {
	                ImageIO.write(SwingFXUtils.fromFXImage(this, null), "png", file);
	            	
	    			PreparedStatement pstmtInsert = null;
	    			String queryInsert = "INSERT INTO image (name, inserted_at) values (?, NOW())";
	    			pstmtInsert = database.connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
	    			pstmtInsert.setString	(1, this.getName());
	    			pstmtInsert.execute();
	    			
	    			try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
	    	            if (generatedKeys.next()) {
	    	                this.setImageId((int)generatedKeys.getLong(1));
	    	            }
	    	            else {
	    	                throw new Exception();
	    	            }
	    	        }
	    			
	            }
	            
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		
		public void remove() throws Exception {
			
			database.connect();
			try {
				String path = Paths.get("").toAbsolutePath().toString();
				String fname= path+"/src/application/assets/images/products/"+this.getName();
				File file = new File(fname);
				
	            if (!file.delete()) {
	            	throw new Exception();
	            }
	            
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		
	}
	
}
