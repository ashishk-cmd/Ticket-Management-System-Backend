package aiims.cf.tms_api.services;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import aiims.cf.tms_api.models.Responsibilities;
import aiims.cf.tms_api.models.Role;
import aiims.cf.tms_api.models.User;
import aiims.cf.tms_api.payload.requests.CategoryAssign;
import aiims.cf.tms_api.payload.requests.ChangePassKey;
import aiims.cf.tms_api.payload.requests.EmployeeRequest;
import aiims.cf.tms_api.payload.requests.GrievenceRequest;
import aiims.cf.tms_api.payload.requests.OtpRequest;
import aiims.cf.tms_api.payload.requests.PassKeyRequest;
import aiims.cf.tms_api.payload.requests.RemarksRequest;
import aiims.cf.tms_api.payload.requests.ResponsibilitiesRequest;
import aiims.cf.tms_api.payload.requests.UserRequest;
import aiims.cf.tms_api.payload.responses.GrievenceResponse;
import aiims.cf.tms_api.payload.responses.RemarksResponse;
import aiims.cf.tms_api.payload.responses.ResponsibilitiesResponse;
import aiims.cf.tms_api.payload.responses.UserResponse;
import aiims.cf.tms_api.payloads.CategoryDto;
import aiims.cf.tms_api.payloads.DepartmentDto;
import aiims.cf.tms_api.payloads.EmpMasterRequest;
import aiims.cf.tms_api.payloads.GrievanceStatus;
import aiims.cf.tms_api.payloads.RoleDto;
import jakarta.validation.Valid;

public interface AdminServices {
	
	//create department
	public DepartmentDto createDepartment(DepartmentDto departmentDto); 	
	//update department
	public DepartmentDto updateDepartment(DepartmentDto departmentDto); 
	//get all departments
	public List<DepartmentDto> getDepartmentList();
	public List<DepartmentDto> getAllDepartmentList();
	
	//create categories
	public CategoryDto addCategory(CategoryDto categoryDto);
	//update categories
	public CategoryDto updateCategory(CategoryDto categoryDto);
	//get only category
	public List<CategoryDto> getCategoryList();
	public List<CategoryDto> getAllCategoryList();
	
	//assign category to department
	public DepartmentDto assignCategoryToDepartment(Long departmentId, Long categoryId);
	public DepartmentDto unassignCategoryToDepartment(Long departmentId, Long categoryId);
	
	
	//create user roles
	public RoleDto createRole(RoleDto roleDto);
	//register new user
	UserResponse registerNewUser(UserRequest user) throws Exception;
	//get user role
//	public Set<Role> getRole(Principal principal);
	public Set<Role> getRole(User user);
	public List<RoleDto> getRoles();
	
	//forget password
	UserResponse forgetUserPassword(EmployeeRequest user) throws Exception;
	//forget password
	UserResponse VerifyOtp(OtpRequest otp);
	//forget password
	UserResponse updatePassword(PassKeyRequest password) throws Exception;	
	
	//change password
	UserResponse changeUserPassword(ChangePassKey password,Principal principal) throws Exception;
	
	//update user
	UserResponse updateUser(UserRequest user, String empId);
	//get user by user id
	UserResponse getEmployeByEmpId(String employeeId);
	//get user by emp id
	//UserResponse getUserByEmployeeId(String employeeId);
	//get user by emp id
	User getUserByEmpId(String employeeId);
	//get all users
	List<UserResponse> getAllUsers();
	//inactive users
	public void inactiveUser(String empId);
	//active user
	public void activeUser(String empId);
	//assign role and responsibilities
	public ResponsibilitiesResponse assignResponsibilities(ResponsibilitiesRequest responsibilitiesRequest);
	public ResponsibilitiesRequest unassignResponsibilities(ResponsibilitiesRequest responsibilitiesRequest);
	
	// create grievance
	public GrievenceResponse createGrievence(GrievenceRequest grievenceDto, Principal principal) throws Exception;
	// check grievance status
	public GrievanceStatus checkGrievenceStatus(GrievanceStatus grievanceStatus) throws Exception;
	// verify grievance status
	public GrievanceStatus verifyGrievenceStatus(GrievanceStatus grievanceStatus);
	// check grievance status
	public GrievenceResponse checkGrievenceStatusByGrievanceRef(String grievanceRef);	
	// assignGrievance to staff
	public GrievenceResponse assignGrievance(String grievanceRefNo,String empId);	
	// update grievance status
	public GrievenceResponse updateGrievanceStatus(String grievanceRefNo,String status);
	
	// get all grievance by employee id
	public Map<String, List<GrievenceResponse>> getGrievenceByEmpId(String empId);		
	//get grievance list department and category wise to help desk
	public Map<String, List<GrievenceResponse>> getGrievancesByDepartmentWise(Principal principal);
	public Map<String, List<GrievenceResponse>> getGrievancesToStaff(Principal principal);
	//to add chats regarding grievances
	public List<RemarksResponse> addRemarks(RemarksRequest remarksRequest,Principal principal);
	
	public EmpMasterRequest createNewUser(EmpMasterRequest body, int randomNumber,String randomKey);
	
	public Map<String, List<CategoryDto>> categogyCanBeAssign(CategoryAssign categoryAssign);
	
	public List<ResponsibilitiesResponse> canAssignGrievanceTo(ResponsibilitiesRequest responsibilitiesRequest);
	
	public ResponseEntity<Object> callExternalAPiFunc(@Valid EmpMasterRequest empMasterRequest) throws Exception;
	
	
//	public void sendMessage(String contactNo,String message) throws Exception;
	
//	public int generateRandomNumber();
	
	
}
