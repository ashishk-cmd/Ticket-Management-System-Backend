package aiims.cf.tms_api.payload.responses;

public class RemarksResponse {

	private UserResponse toUser;
	private UserResponse fromUser;
	private String remarks;
	public UserResponse getToUser() {
		return toUser;
	}
	public void setToUser(UserResponse toUser) {
		this.toUser = toUser;
	}
	public UserResponse getFromUser() {
		return fromUser;
	}
	public void setFromUser(UserResponse fromUser) {
		this.fromUser = fromUser;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
