package application.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ProductImage extends Image{
	
	private DB database = new DB();
	
	private Integer index;
	private Integer id;
	private Integer imageId;
	private Integer productId;
	private String name;
	public boolean remove = false;
	
	public ProductImage(String string) {
		super(string);
	}
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
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
	
	public void save() throws Exception {
		
		database.connect();
		
		String ext = this.getName().split("\\.")[1];
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String imageName = UtilMethods.generateRandomString(6) + "_" + timestamp.getTime();
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
    	                throw new SQLException("Creating user failed, no ID obtained.");
    	            }
    	        }
    			
            }
            
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}catch (IOException e) {
	    	throw new Exception(e.getMessage());
	    }finally {
			database.disconnect();
		}
	}
	
}
