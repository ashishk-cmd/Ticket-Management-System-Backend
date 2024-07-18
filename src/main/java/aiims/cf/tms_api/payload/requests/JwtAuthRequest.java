package aiims.cf.tms_api.payload.requests;

import jakarta.validation.constraints.NotEmpty;

public class JwtAuthRequest {

	@NotEmpty(message="Please provide a User Name.")
	private String username;
	@NotEmpty(message="Please provide a Password.")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
