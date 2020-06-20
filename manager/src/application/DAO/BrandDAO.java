package application.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import application.models.Brand;

public class BrandDAO {
	public static ArrayList<Brand> getAll() throws Exception{
		ArrayList<Brand> brands = new ArrayList<Brand>();
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT ";
		query += "* ";
		query += "FROM brand ";
		query += "ORDER BY id DESC ";
		
		if(database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					brands = resultSetToBrandList(resultSet);	
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return brands;
	}
	
	public static Brand insert( Brand brand ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "INSERT INTO brand ";
		query += "(name) VALUES ";
		query += "(?) ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				
				preparedStatement.setString(1, brand.name);
				preparedStatement.execute();
				
				try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
    	            if (generatedKeys.next()) {
    	                brand.id = (int)generatedKeys.getLong(1);
    	            } else {
    	                throw new Exception();
    	            }
    	        }
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		
		return brand;
	}
	
	public static Brand update( Brand brand ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "UPDATE brand ";
		query += "SET name = ? ";
		query += "WHERE id = ? ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				preparedStatement.setString(1, brand.name);
				preparedStatement.setInt(2, brand.id);
				if(preparedStatement.executeUpdate() <= 0) {
					throw new Exception("Brand not updated!");
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}else {
			throw new Exception("Brand not updated!");
		}
		return brand;
	}
	
	public static void delete( Integer id ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "DELETE FROM brand ";
		query += "WHERE id = ? ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);

				preparedStatement.setInt(1, id);

				preparedStatement.execute();
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}else {
			throw new Exception("Brand not deleted!");
		}
	}
	
	/*private static Brand resultSetToBrand(ResultSet resultSet) throws Exception{
		Brand brand = null;
		try {
			brand = new Brand(resultSet.getInt("id"), resultSet.getString("name"));
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
		return brand;
	}*/
	
	private static ArrayList<Brand> resultSetToBrandList(ResultSet resultSet) throws Exception{
		ArrayList<Brand> brands = new ArrayList<Brand>();
		
		while (resultSet.next()) {
			try {
				Brand brand = new Brand(resultSet.getInt("id"), resultSet.getString("name"));
				brands.add(brand);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return brands;
	}
}
