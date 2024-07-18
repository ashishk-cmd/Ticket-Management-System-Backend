package aiims.cf.tms_api.payload.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class PassKeyRequest {

	@NotEmpty(message="Please provide a Employee Id.")
	@Pattern(regexp = "^[a-zA-Z]{1}[0-9]{7}$",message = "Employee Id must be of 8 length with no special characters and first letter must be alphabet.")	
	private String employeeId;
		
	@NotEmpty(message="Please provide a Mobile Number.")
	@Pattern(regexp = "^[0-9]{10}$",message = "Mobile Number must be of 10 numbers !!")
	private String contactNo;
	
	private int otp;
	
	private String password;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
