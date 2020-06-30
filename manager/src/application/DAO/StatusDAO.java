package application.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import application.models.User.Status;

public class StatusDAO {
	public static ArrayList<Status> getAll() throws Exception{
		ArrayList<Status> statuses = new ArrayList<Status>();
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT ";
		query += "* ";
		query += "FROM user_status ";
		query += "ORDER BY id DESC ";
		
		if(database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					statuses = resultSetToStatusList(resultSet);	
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return statuses;
	}
	
	public static Status insert( Status status ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "INSERT INTO user_status ";
		query += "(name) VALUES ";
		query += "(?) ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				
				preparedStatement.setString(1, status.name);
				preparedStatement.execute();
				
				try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
    	            if (generatedKeys.next()) {
    	                status.id = (int)generatedKeys.getLong(1);
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
		
		return status;
	}
	
	public static Status update( Status status ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "UPDATE user_status ";
		query += "SET name = ? ";
		query += "WHERE id = ? ";
		
		if(database.connection != null) {
			try{	
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				preparedStatement.setString(1, status.name);
				preparedStatement.setInt(2, status.id);
				if(preparedStatement.executeUpdate() <= 0) {
					throw new Exception("Status not updated!");
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
		return status;
	}
	
	public static void delete( Integer id ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "DELETE FROM user_status ";
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
	
	private static ArrayList<Status> resultSetToStatusList(ResultSet resultSet) throws Exception{
		ArrayList<Status> statuses = new ArrayList<Status>();
		
		while (resultSet.next()) {
			try {
				Status status = new Status(resultSet.getInt("id"), resultSet.getString("name"));
				statuses.add(status);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return statuses;
	}
}
