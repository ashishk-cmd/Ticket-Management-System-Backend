package aiims.cf.tms_api.payload.responses;

import java.util.Set;

import aiims.cf.tms_api.models.Role;

public class JwtAuthResponse {
	private String empId;
	private String username;
	private String token;
	private Set<Role> role;
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Set<Role> getRole() {
		return role;
	}
	public void setRole(Set<Role> role) {
		this.role = role;
	}
	
}
