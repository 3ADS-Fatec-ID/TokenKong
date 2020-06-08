package application.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private Integer id;
	private String name;
	private UserRole role;
	private UserStatus status;
	private String email;
	private String password;
	
	public User() {}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
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
				
				preparedStatement.setString(1,this.name);
				preparedStatement.setString(2,this.name);
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
