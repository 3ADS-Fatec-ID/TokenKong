package application.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import application.models.Category;

public class CategoryDAO {
	public static ArrayList<Category> getAll() throws Exception{
		ArrayList<Category> categories = new ArrayList<Category>();
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT ";
		query += "* ";
		query += "FROM category ";
		query += "ORDER BY id DESC ";
		
		if(database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					categories = resultSetToCategoryList(resultSet);	
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return categories;
	}
	
	public static Category insert( Category category ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "INSERT INTO category ";
		query += "(name) VALUES ";
		query += "(?) ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				
				preparedStatement.setString(1, category.name);
				preparedStatement.execute();
				
				try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
    	            if (generatedKeys.next()) {
    	            	category.id = (int)generatedKeys.getLong(1);
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
		
		return category;
	}
	
	public static Category update( Category category ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "UPDATE category ";
		query += "SET name = ? ";
		query += "WHERE id = ? ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				preparedStatement.setString(1, category.name);
				preparedStatement.setInt(2, category.id);
				if(preparedStatement.executeUpdate() <= 0) {
					throw new Exception("Category not updated!");
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}else {
			throw new Exception("Category not updated!");
		}
		return category;
	}
	
	public static void delete( Integer id ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "DELETE FROM category ";
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
			throw new Exception("The category has not been removed!");
		}
	}
	
	private static ArrayList<Category> resultSetToCategoryList(ResultSet resultSet) throws Exception{
		ArrayList<Category> categories = new ArrayList<Category>();
		
		while (resultSet.next()) {
			try {
				Category category = new Category(resultSet.getInt("id"), resultSet.getString("name"));
				categories.add(category);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return categories;
	}
}
