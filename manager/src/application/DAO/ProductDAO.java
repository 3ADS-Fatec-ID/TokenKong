package application.DAO;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.models.Product;
import application.models.ProductImage;

public class ProductDAO {
	
	public static ArrayList<Product> getProducts(){
		
		ArrayList<Product> products = new ArrayList<Product>();
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT ";
		query += "P.*, ";
		query += "I.name as image, ";
		query += "I.id as image_id, ";
		query += "PI.id as product_image_id ";
		query += "FROM product as P ";
		query += "LEFT JOIN product_image as PI ";
		query += "ON PI.product_id = P.id ";
		query += "LEFT JOIN image as I ";
		query += "ON I.id = PI.image_id ";
		query += "GROUP BY P.id ";
		
		if(database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					if(!resultSet.equals(null)) {
						products = resultSetToProducts(resultSet);	
					}
				}
				
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}finally {
				database.disconnect();
			}	
		}
		return products;
	}
	
	private static ArrayList<Product> resultSetToProducts(ResultSet resultSet) throws SQLException{
		ArrayList<Product> products = new ArrayList<Product>();
		
		while (resultSet.next()) {
			Product product = new Product();
			try {
				
				product.setId(resultSet.getInt("id"));
				product.setName(resultSet.getString("name"));
				product.setPrice(resultSet.getDouble("price"));
				product.setQuantity(resultSet.getInt("quantity"));
				
				if(resultSet.getString("image") != null) {
					
					File temp = new File("src\\application\\assets\\images\\products\\"+resultSet.getString("image"));

	                temp.toURI().toString();
	                
					ProductImage image = new ProductImage(temp.toURI().toString());
					image.setProductId(resultSet.getInt("id"));
					image.setImageId(resultSet.getInt("image_id"));
					image.setId(resultSet.getInt("product_image_id"));
					image.setName(resultSet.getString("image"));
					
					ArrayList<ProductImage> imageList = new ArrayList<ProductImage>();
					imageList.add(image);
					product.setImages(imageList);
				}
				
				products.add(product);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return products;
	}
}
