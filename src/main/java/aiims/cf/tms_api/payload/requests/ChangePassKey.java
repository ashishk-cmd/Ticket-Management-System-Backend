package aiims.cf.tms_api.payload.requests;

import jakarta.validation.constraints.NotEmpty;

public class ChangePassKey {

	@NotEmpty(message="Please provide a Old password.")
	private String oldPassword;

	@NotEmpty(message="Please provide a New Password.")
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
