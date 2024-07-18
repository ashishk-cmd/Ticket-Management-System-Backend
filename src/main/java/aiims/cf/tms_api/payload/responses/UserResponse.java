package aiims.cf.tms_api.payload.responses;


import java.util.HashSet;
import java.util.Set;

import aiims.cf.tms_api.models.Department;
import aiims.cf.tms_api.payloads.RoleDto;


public class UserResponse {

	private int id;
	private String fullName;
	private String employeeId;
	private String contactNo;
	private String email;
	private String status;
	private String password;
//	private String registeredOn;
//	private String otpSentOn;
	private Department department;
	
	
	private Set<RoleDto> roles = new HashSet<>();


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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


	public String getContactNo() {
		return contactNo;
	}


	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}


	public Set<RoleDto> getRoles() {
		return roles;
	}


	public void setRoles(Set<RoleDto> roles) {
		this.roles = roles;
	}


//	public UserResponse(int id, String fullName, String employeeId, String contactNo, String email, String status,
//			String regOn, String otpSentOn, Department department, Set<RoleDto> roles) {
//		super();
//		this.id = id;
//		this.fullName = fullName;
//		this.employeeId = employeeId;
//		this.contactNo = contactNo;
//		this.email = email;
//		this.status = status;
//		this.registeredOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(regOn);
//		this.otpSentOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(otpSentOn);
//		this.department = department;
//		this.roles = roles;
//	}
	
	
	
	
	
}