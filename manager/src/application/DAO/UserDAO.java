package application.DAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.models.User;
import application.models.User.Role;
import application.models.User.Status;

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
		query += "WHERE (U.name = ? OR U.email = ?) AND U.password = ? ";
		
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
						users = resultSetToUserList(resultSet);
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
	
	public static User getOne(Integer userId) throws Exception{
		User user = null;
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT ";
		query += "U.*, ";
		query += "UR.id as roleId, ";
		query += "UR.name as roleName, ";
		query += "US.id as statusId, ";
		query += "US.name as statusName ";
		query += "FROM user as U ";
		query += "LEFT JOIN user_status as US ";
		query += "ON US.id = U.status_id ";
		query += "LEFT JOIN user_role as UR ";
		query += "ON UR.id = U.role_id ";
		query += "WHERE U.id = ? ";
		
		if(database.connection != null) {
			try(
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
			){				
				
				preparedStatement.setInt(1,userId);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					if(!resultSet.equals(null)) {
						ArrayList<User> users = resultSetToUserList(resultSet); 
						if(users.size() != 0)
							user = users.get(0);
					}
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return user;
	}
	
	public static ArrayList<User> getAll() throws Exception{
		
		ArrayList<User> users = new ArrayList<User>();
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "SELECT ";
		query += "U.*, ";
		query += "UR.id as roleId, ";
		query += "UR.name as roleName, ";
		query += "US.id as statusId, ";
		query += "US.name as statusName ";
		query += "FROM user as U ";
		query += "LEFT JOIN user_status as US ";
		query += "ON US.id = U.status_id ";
		query += "LEFT JOIN user_role as UR ";
		query += "ON UR.id = U.role_id ";
		query += "GROUP BY U.id ORDER BY U.id DESC";
		
		if(database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				
				if (preparedStatement.execute()) {
					ResultSet resultSet = preparedStatement.getResultSet();
					users = resultSetToUserList(resultSet);	
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw e;
			}finally {
				database.disconnect();
			}
		}
		return users;
	}
	
	public static User create(User user) throws Exception{
		
		DB database = new DB();
		database.connect();
		PreparedStatement pstmtCreate = null;
		Integer userId = null;
		
		if(database.connection != null) {
			try {
				
				String queryCreate = "";
				queryCreate += "INSERT INTO user ( ";
				queryCreate += "name, ";
				queryCreate += "email, ";
				queryCreate += "password ";
				if( user.getRole().id != null )
					queryCreate += ", role_id ";
				if( user.getStatus().id != null )
					queryCreate += ", status_id ";
				queryCreate += ") values ( ?, ?, ? ";
				if( user.getRole().id != null )
					queryCreate += ", ? ";
				if( user.getStatus().id != null )
					queryCreate += ", ? ";
				queryCreate += ") ";
				
				pstmtCreate = database.connection.prepareStatement(queryCreate, Statement.RETURN_GENERATED_KEYS);
				
				pstmtCreate.setString	(1, user.getName());
				pstmtCreate.setString	(2, user.getEmail());
				pstmtCreate.setString	(3, user.getPassword());
				int i = 4;
				if( user.getRole().id != null ) {
					pstmtCreate.setInt	(i, user.getRole().id);
					i += 1;
				}
				if( user.getStatus().id != null )
					pstmtCreate.setInt	(i, user.getStatus().id);
				
				pstmtCreate.execute();
				 
				ResultSet resultSet = pstmtCreate.getGeneratedKeys();
				if (resultSet.next()) {
					userId = resultSet.getInt(1);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				throw e;
			}finally {
				if(pstmtCreate != null && !pstmtCreate.isClosed()) {
					pstmtCreate.close();
				}
				database.disconnect();
			}
		}
		
		if(!userId.equals(null)) {
			try {
				user = getOne(userId);
			}catch(Exception e) {
				System.out.println(e.getMessage());
				user = null;
			}
		}else {
			user = null;
		}
		
		return user;
	}
	
	public static User update(User user) throws Exception{
		
		DB database = new DB();
		database.connect();
		PreparedStatement pstmtUpdate = null;
		
		if(database.connection != null) {
			try {
				
				String queryUpdate = "";
				queryUpdate += "UPDATE user SET ";
				queryUpdate += "name = ?, ";
				queryUpdate += "email = ?, ";
				queryUpdate += "password = ? ";
				if( user.getRole().id != null )
					queryUpdate += ", role_id = ? ";
				if( user.getStatus().id != null )
					queryUpdate += ", status_id = ? ";
				queryUpdate += "WHERE id = ? ";
				
				pstmtUpdate = database.connection.prepareStatement(queryUpdate);
				
				pstmtUpdate.setString	(1, user.getName());
				pstmtUpdate.setString	(2, user.getEmail());
				pstmtUpdate.setString	(3, user.getPassword());
				int i = 4;
				if( user.getRole().id != null ) {
					pstmtUpdate.setInt	(i, user.getRole().id);
					i += 1;
				}
				if( user.getStatus().id != null ) {
					pstmtUpdate.setInt	(i, user.getStatus().id);
					i += 1;
				}
				
				pstmtUpdate.setInt	(i, user.getId());
				
				pstmtUpdate.execute();
				
			}catch(Exception e) {
				e.printStackTrace();
				throw e;
			}finally {
				if(pstmtUpdate != null && !pstmtUpdate.isClosed()) {
					pstmtUpdate.close();
				}
				database.disconnect();
			}
		}
		
		try {
			user = getOne(user.getId());
		}catch(Exception e) {
			System.out.println(e.getMessage());
			user = null;
		}
		
		return user;
	}
	
	public static void delete( Integer id ) throws Exception{
		
		DB database = new DB();
		database.connect();
		
		String query = "";
		query += "DELETE FROM user ";
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
			throw new Exception("The user has not been removed!");
		}
	}
	
	private static ArrayList<User> resultSetToUserList(ResultSet resultSet) throws SQLException{
		ArrayList<User> users = new ArrayList<User>();
		
		while (resultSet.next()) {
			User user = new User();
			try {
				user.setEmail( resultSet.getString("email") );
				user.setName( resultSet.getString("name") );
				user.setId( resultSet.getInt("id") );
				user.setPassword( resultSet.getString("password") );
				user.setRole(new Role(
					resultSet.getInt("roleId"),
					resultSet.getString("roleName")
				));
				user.setStatus(new Status(
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
	
	public static User getLocalUserData() throws Exception {
		User user = new User();
		String path = Paths.get("").toAbsolutePath().toString();
		String fname= path+"/src/application/assets/userData.ser";
		File file = new File(fname);
		if(file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(fname));
			Integer userId = Integer.parseInt(reader.readLine());
			if(userId != null) {
			    user.setId(userId);
			    user.setName(reader.readLine());
			    user.setEmail(reader.readLine());
			    Role role = new Role(
			    	Integer.parseInt(reader.readLine()),
			    	reader.readLine()
			    );
			    Status status = new Status(
			    	Integer.parseInt(reader.readLine()),
			    	reader.readLine()
			    );
			    user.setRole(role);
			    user.setStatus(status);
			    user.setPassword(reader.readLine());
			}
			reader.close();
		}
		return user;
	} 
	
	public static void saveLocalUserData(User user) throws Exception {
		String path = Paths.get("").toAbsolutePath().toString();
		String fname= path+"/src/application/assets/userData.ser";
	    PrintWriter writer = new PrintWriter(fname);
	    writer.println(user.getId());
	    writer.println(user.getName());
	    writer.println(user.getEmail());
	    writer.println(user.getRole().id);
	    writer.println(user.getRole().name);
	    writer.println(user.getStatus().id);
	    writer.println(user.getStatus().name);
	    writer.println(user.getPassword());
	    writer.close();
	}
	
	public static void deleteLocalUserData() throws Exception {
		String path = Paths.get("").toAbsolutePath().toString();
		String fname= path+"/src/application/assets/userData.ser";
		File file = new File(fname);
		if(file.exists()) {
			PrintWriter writer = new PrintWriter(fname);
		    writer.close();
		}
	}
	
	public static boolean isUserLogged() throws Exception {
		boolean result = false;

		String path = Paths.get("").toAbsolutePath().toString();
		String fname= path+"/src/application/assets/userData.ser";
		
		if(new File(fname).exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(fname));
			result = (reader.readLine() != null);
			reader.close();
		}

		return result;
	}
}
