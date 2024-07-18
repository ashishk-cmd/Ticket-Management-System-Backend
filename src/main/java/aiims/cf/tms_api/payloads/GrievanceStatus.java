package aiims.cf.tms_api.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class GrievanceStatus {

	@NotEmpty(message="Please provide a Employee Id.")
	@Pattern(regexp = "^[a-zA-Z]{1}[0-9]{7}$",message = "Employee Id must be of 7 length with no special characters and first letter must be alphabet.")	
	private String employeeId;
	
	@NotEmpty(message="Please provide a Grievance Refrence Number.")
	private String grievanceRef;
	private String status;
	private int otp;
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getGrievanceRef() {
		return grievanceRef;
	}
	public void setGrievanceRef(String grievanceRef) {
		this.grievanceRef = grievanceRef;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	
	
}
