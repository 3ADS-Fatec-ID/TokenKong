package application.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import application.models.User.Role;

public class RoleDAO {
	public static ArrayList<Role> getAll() throws Exception{
		ArrayList<Role> roles = new ArrayList<Role>();
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT ";
		query += "* ";
		query += "FROM user_role ";
		query += "ORDER BY id DESC ";
		
		if(database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					roles = resultSetToRoleList(resultSet);	
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return roles;
	}
	
	public static Role insert( Role role ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "INSERT INTO role ";
		query += "(name) VALUES ";
		query += "(?) ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				
				preparedStatement.setString(1, role.name);
				preparedStatement.execute();
				
				try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
    	            if (generatedKeys.next()) {
    	                role.id = (int)generatedKeys.getLong(1);
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
		
		return role;
	}
	
	public static Role update( Role role ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "UPDATE role ";
		query += "SET name = ? ";
		query += "WHERE id = ? ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				preparedStatement.setString(1, role.name);
				preparedStatement.setInt(2, role.id);
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
			throw new Exception("Status not updated!");
		}
		return role;
	}
	
	public static void delete( Integer id ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "DELETE FROM role ";
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
			throw new Exception("Status not deleted!");
		}
	}
	
	private static ArrayList<Role> resultSetToRoleList(ResultSet resultSet) throws Exception{
		ArrayList<Role> roles = new ArrayList<Role>();
		
		while (resultSet.next()) {
			try {
				Role role = new Role(resultSet.getInt("id"), resultSet.getString("name"));
				roles.add(role);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return roles;
	}
}
