package application.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	private String user = "root";
	private String database = "tokenkong";
	private String password = "123456";
	private String host = "localhost:3306";
	
	public Connection connection = null;
	
	public void connect() {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://"+this.host+"/"+this.database+"?useTimezone=true&serverTimezone=UTC",this.user, this.password);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			this.connection = null;
		}
	}
	
	public void disconnect() {
		try {
			if(this.connection != null) {
				if(!this.connection.isClosed()) {
					this.connection.close();	
					this.connection = null;
				}
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			this.connection = null;
		}
	}
}
