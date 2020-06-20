package application.DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.models.Product;
import application.models.ProductImage;

public class ProductDAO {
	
	public static Product getOne(Integer productId) throws Exception{
		Product product = null;
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
		query += "WHERE P.id = ? ";
		
		if(database.connection != null) {
			try(
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
			){				
				
				preparedStatement.setInt(1,productId);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					if(!resultSet.equals(null)) {
						product = resultSetToProduct(resultSet);
					}
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return product;
	}
	
	public static ArrayList<Product> getAll() throws Exception{
		
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
					products = resultSetToProductList(resultSet);	
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return products;
	}
	
	public static Product update(Product product) throws Exception{
		
		DB database = new DB();
		database.connect();
		PreparedStatement pstmtUpdate = null;
		
		if(database.connection != null) {
			try {				
				database.connection.setAutoCommit(false);
				
				String queryUpdate = "";
				queryUpdate += "UPDATE product SET ";
				queryUpdate += "name = ?, ";
				queryUpdate += "price = ?, ";
				queryUpdate += "quantity = ?, ";
				queryUpdate += "description = ? ";
				queryUpdate += "WHERE id = ? ";
				
			    pstmtUpdate = database.connection.prepareStatement(queryUpdate);
				
				pstmtUpdate.setString	(1, product.getName());
				pstmtUpdate.setDouble	(2, product.getPrice());
				pstmtUpdate.setInt		(3, product.getQuantity());
				pstmtUpdate.setString	(4, product.getDescription());
				pstmtUpdate.setInt		(5, product.getId());

				pstmtUpdate.execute();
				
				ArrayList<ProductImage> images = product.getImages();
				ArrayList<ProductImage> imagesToRemove = new ArrayList<ProductImage>();
				ArrayList<ProductImage> imagesToSave = new ArrayList<ProductImage>();
				
				if(!images.isEmpty()) {
					for(ProductImage image: images) {
						Integer id = image.getId();
						if(image.remove == true && id != null) {
							imagesToRemove.add(image);
						}else if(image.remove == false && id == null) {
							image.setProductId(product.getId());
							imagesToSave.add(image);
						}
					}
				}
				
				if(!imagesToRemove.equals(null) && !imagesToRemove.isEmpty()) {
					deleteProductImages(imagesToRemove, database.connection);
				}
				
				if(!imagesToSave.equals(null) && !imagesToSave.isEmpty()) {
					saveProductImages(imagesToSave, database.connection);
				}
				
				database.connection.commit();
				
			}catch(Exception e) {
				e.printStackTrace();
				database.connection.rollback();
				throw e;
			}finally {
				if(pstmtUpdate != null && !pstmtUpdate.isClosed()) {
					pstmtUpdate.close();
				}
				database.disconnect();
			}
		}
		
		try {
			Thread.sleep(5000);
			product = getOne(product.getId());
		}catch(Exception e) {
			System.out.println(e.getMessage());
			product = null;
		}
		
		return product;
	}
	
	private static void saveProductImages(ArrayList<ProductImage> images, Connection connection ) throws Exception {
		
		PreparedStatement pstmtInsert = null;
		
		try {
			String queryInsert = "";
			queryInsert += "INSERT INTO product_image ( ";
			queryInsert += "image_id, ";
			queryInsert += "product_id ";
			queryInsert += ") VALUES ";
			
			for(int i = 0; i < images.size(); i++) {
				images.get(i).save();
				if(i == 0) {
					queryInsert += "( ?, ? ) ";
				}else {
					queryInsert += ",( ?, ? ) ";							
				}
			}
			
			int parameterIndex = 1;
			pstmtInsert = connection.prepareStatement(queryInsert);
			for(int i = 0; i < images.size(); i++) {
				pstmtInsert.setInt(parameterIndex, images.get(i).getImageId() );
				parameterIndex += 1;
				pstmtInsert.setInt(parameterIndex , images.get(i).getProductId() );
				parameterIndex += 1;
			}
			pstmtInsert.execute();
			
		}catch(Exception e) {
			throw e;
		}finally {
			if(pstmtInsert != null && !pstmtInsert.isClosed()) {
				pstmtInsert.close();
			}
		}
	}
	
	private static void deleteProductImages(ArrayList<ProductImage> images, Connection connection ) throws SQLException {
		
		PreparedStatement pstmtRemove = null;
		
		try {			
			String queryRemove = "";
			queryRemove += "DELETE FROM ";
			queryRemove += "product_image WHERE ";
			queryRemove += "id IN ( ";
			
			for(int i = 0; i < images.size(); i++) {
				try {
					images.get(i).remove();
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
				if(i > 0) queryRemove += ", ";
				queryRemove += "?";
			}
			
			queryRemove += " ) ";
			
			pstmtRemove = connection.prepareStatement(queryRemove);
			for(int i = 0; i < images.size(); i++) {
				pstmtRemove.setInt( i + 1 , images.get(i).getId() );
			}
	        
			pstmtRemove.execute();
			
		}catch(SQLException e) {
			throw e;
		}finally {
			if(pstmtRemove != null && !pstmtRemove.isClosed()) {
				pstmtRemove.close();
			}
		}
	}
	
	private static Product resultSetToProduct(ResultSet resultSet) throws SQLException{
		Product product = null;
		if(!resultSet.equals(null)) {
			
			product = new Product();
			ArrayList<ProductImage> productImages = new ArrayList<ProductImage>();
			while (resultSet.next()) {
				if(resultSet.isLast()) {
					product.setId(resultSet.getInt("id"));
					product.setName(resultSet.getString("name"));
					product.setPrice(resultSet.getDouble("price"));
					product.setQuantity(resultSet.getInt("quantity"));					
				}
				if(resultSet.getString("image") != null) {
					ProductImage productImage = new ProductImage("/application/assets/images/products/"+resultSet.getString("image"));
					productImage.setName(resultSet.getString("image"));
					productImage.setImageId(resultSet.getInt("image_id"));
					productImage.setProductId(resultSet.getInt("id"));
					productImage.setId(resultSet.getInt("product_image_id"));
					productImages.add(productImage);
				}
			}
			product.setImages(productImages);
		}
		
		return product;
	}
	
	private static ArrayList<Product> resultSetToProductList(ResultSet resultSet) throws SQLException{
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
