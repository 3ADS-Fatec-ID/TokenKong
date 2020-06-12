package application.models;

import application.DAO.UserDAO;

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
		try {
			result = UserDAO.getUserByEmailAndPassword(this.name, this.password) != null;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			//show error message
		}
		
		return result;
	}

}