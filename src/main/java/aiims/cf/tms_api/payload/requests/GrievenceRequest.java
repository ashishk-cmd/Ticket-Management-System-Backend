package aiims.cf.tms_api.payload.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class GrievenceRequest  
{
// Grievance Details
	private String refNo;
	@Min(value = 1, message = "Please select a Department.")
	private Long departmentId;
	@Min(value = 1, message = "Please provide a Category.")
	private Long categoryId;
	@NotEmpty(message="Please provide a Concern.")
	private String concern;
	private String grievanceStatus;
	private String image1;
	private String image2;
	
// User Details
	@NotEmpty(message="Please provide a Name.")
	private String fullName;
	@NotEmpty(message="Please provide a Employee Id.")
	private String employeeId;
	
	private String email;
	@NotEmpty(message="Please provide a Contact Number.")
	private String contactNo;
	@NotNull(message = "Please provide sent Otp.")
	private int otp;
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getConcern() {
		return concern;
	}
	public void setConcern(String concern) {
		this.concern = concern;
	}
	public String getGrievanceStatus() {
		return grievanceStatus;
	}
	public void setGrievanceStatus(String grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	
	
	
	
}
