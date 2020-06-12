package application.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.models.User;
import application.models.UserRole;
import application.models.UserStatus;

public class UserDAO {
	
	public static User getUserByEmailAndPassword(String username, String password) throws Exception{

		if(username.isEmpty() || password.isEmpty()) throw new Exception("The fields are empty!");
		
		ArrayList<User> users = new ArrayList<User>();
		User user = null;
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT U.*, ";
		query += "UR.id as roleId, ";
		query += "UR.name as roleName, ";
		query += "US.id as statusId, ";
		query += "US.name as statusName ";
		query += "FROM user as U ";
		query += "INNER JOIN user_role as UR ";
		query += "ON UR.id = U.role_id ";
		query += "INNER JOIN user_status as US ";
		query += "ON US.id = U.status_id ";
		query += "WHERE (U.name = ? OR U.email = ?) AND U.password = ? AND U.status_id NOT IN (3, 4) ";
		
		if(database.connection != null) {
			
			try(
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
			){				
				
				preparedStatement.setString(1,username);
				preparedStatement.setString(2,username);
				preparedStatement.setString(3,password);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					if(!resultSet.equals(null)) {
						users = resultSetToUsers(resultSet);
						if(users.size() == 1) {
							user = users.get(0);
						}
					}
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}	
		}
		return user;
	}
	
	private static ArrayList<User> resultSetToUsers(ResultSet resultSet) throws SQLException{
		ArrayList<User> users = new ArrayList<User>();
		
		while (resultSet.next()) {
			User user = new User();
			try {
				user.setEmail( resultSet.getString("email") );
				user.setName( resultSet.getString("name") );
				user.setId( resultSet.getInt("id") );
				user.setPassword( resultSet.getString("password") );
				user.setRole(new UserRole(
					resultSet.getInt("roleId"),
					resultSet.getString("roleName")
				));
				user.setStatus(new UserStatus(
					resultSet.getInt("statusId"),
					resultSet.getString("statusName")
				));
				users.add(user);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return users;
	}
}
