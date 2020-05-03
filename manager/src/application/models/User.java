package application.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	public String userName;
	public String password;
	
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public boolean isAuthenticated(){
		
		boolean result = false;
		
		DB database = new DB();
		database.connect();
		ResultSet resultSet = null;
		
		String query = "SELECT * FROM USER WHERE (name = ? OR email = ?) AND password = ? ";
		
		if(database.connection != null) {
			try(
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
			){				
				
				preparedStatement.setString(1,this.userName);
				preparedStatement.setString(2,this.userName);
				preparedStatement.setString(3,this.password);
				
				if (preparedStatement.execute()) {
					
					resultSet = preparedStatement.getResultSet();
					if(resultSet != null) {
						
						int resultLength = 0;
						while (resultSet.next()) {
							resultLength++;
						}
						
						if(resultLength > 0) {
							result = true;
						}else {
							result = false;
						}
					}
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}finally {
				database.disconnect();
			}	
		}
		
		return result;
	}
}
