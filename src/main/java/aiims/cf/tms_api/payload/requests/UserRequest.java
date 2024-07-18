package aiims.cf.tms_api.payload.requests;


import java.util.HashSet;
import java.util.Set;

import aiims.cf.tms_api.payloads.RoleDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UserRequest {

	private int id;
	
	@NotEmpty(message="Please provide a User Name.")
	@Pattern(regexp = "^[a-zA-Z0-9 ]{3,}$", message = "User Name must be at least 3 characters long with no special characters.")
	private String fullName;
	
	@NotEmpty(message="Please provide a Employee Id.")
	@Pattern(regexp = "^[a-zA-Z]{1}[0-9]{7}$",message = "Employee Id must be of 8 length with no special characters and first letter must be alphabet.")	
	private String employeeId;
	
	private Long departmentId;
	private Integer roleId;
	
	@NotEmpty(message="Please provide a Mobile Number.")
	@Pattern(regexp = "^[0-9]{10}$",message = "Mobile Number must be of 10 numbers !!")
	private String contactNo;
	
	@NotEmpty(message = "Please provide a Email Id.")
	@Email(message = "Email address is not valid.")
	private String email;

//	private String status;
	
//	private Boolean enabled;
	
//	@NotEmpty(message="Please provide a Password.")
//	@Size(min = 3 ,max = 10 ,message = "Password must be min of 3 chars and max of 10 chars !!")
//	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$",
//    message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
//	private String password;
//
//	private Date registeredOn;
//	private Date otpSentOn; 
//	private int otp;
	
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

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public Set<RoleDto> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDto> roles) {
		this.roles = roles;
	}
	
	
}