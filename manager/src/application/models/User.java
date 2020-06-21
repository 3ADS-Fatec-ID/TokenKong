package application.models;

import application.DAO.UserDAO;

public class User {
	private Integer id;
	private String name;
	private Role role;
	private Status status;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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
	
	public static class Role {
		public Integer id;
		public String name;
		
		public Role(Integer id, String name) {
			this.id = id;
			this.name = name;
		}
	}
	
	public static class Status {
		public Integer id;
		public String name;
		
		public Status(Integer id, String name) {
			this.id = id;
			this.name = name;
		}
	}
}