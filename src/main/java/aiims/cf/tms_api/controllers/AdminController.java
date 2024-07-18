package aiims.cf.tms_api.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import aiims.cf.tms_api.models.Responsibilities;
import aiims.cf.tms_api.models.User;
import aiims.cf.tms_api.payload.requests.AssignGrievance;
import aiims.cf.tms_api.payload.requests.CategoryAssign;
import aiims.cf.tms_api.payload.requests.ChangePassKey;
import aiims.cf.tms_api.payload.requests.EmpRequest;
import aiims.cf.tms_api.payload.requests.GrievenceRequest;
import aiims.cf.tms_api.payload.requests.RemarksRequest;
import aiims.cf.tms_api.payload.requests.ResponsibilitiesRequest;
import aiims.cf.tms_api.payload.requests.UserRequest;
import aiims.cf.tms_api.payload.responses.GrievenceResponse;
import aiims.cf.tms_api.payload.responses.RemarksResponse;
import aiims.cf.tms_api.payload.responses.ResponsibilitiesResponse;
import aiims.cf.tms_api.payload.responses.UserResponse;
import aiims.cf.tms_api.payloads.ApiResponse;
import aiims.cf.tms_api.payloads.CategoryDto;
import aiims.cf.tms_api.payloads.DepartmentDto;
import aiims.cf.tms_api.payloads.EmpMasterRequest;
import aiims.cf.tms_api.payloads.GrievanceStatus;
import aiims.cf.tms_api.payloads.RoleDto;
import aiims.cf.tms_api.repositories.UserRepo;
import aiims.cf.tms_api.services.AdminServices;
import aiims.cf.tms_api.utils.ApplicationUtility;
import aiims.cf.tms_api.utils.CryptoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminServices adminservices;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	
	 	@GetMapping("/server-status")
	    public String getStatus() {
	        return "Server started";
	    }
	
		@GetMapping("/error")
	    public void throwError() {
			throw new RuntimeException("This is a deliberate error.");   
	    }
	
		@PostMapping("/encrypt")
	    public String encryptPassword(@RequestBody String plainText) {
	        return CryptoUtil.encrypt(plainText);
	    }

	    @PostMapping("/decrypt")
	    public String decryptPassword(@RequestBody String encryptedText) {
	        return CryptoUtil.decrypt(encryptedText);
	    }
	    
	    
		@PostMapping("/checkemp")
		public ResponseEntity<Object> callExternalApi(@Valid @RequestBody EmpMasterRequest empMasterRequest) throws Exception 
		{
			try {
				ResponseEntity<Object> ret = this.adminservices.callExternalAPiFunc(empMasterRequest);
				return ret;				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
	    }
		

		@PostMapping("/logout")
	    public ResponseEntity<?> logout(HttpServletRequest request) {
	        String authToken = request.getHeader("Authorization");
	        if (authToken != null && authToken.startsWith("Bearer ")) {
	            // Remove Bearer from token
	            authToken = authToken.substring(7);
	            // Add token to blacklist in cache with a 10-minute expiration time
	            redisTemplate.opsForValue().set(authToken, "loggedOut", 10, TimeUnit.MINUTES);
	        }
	        return ResponseEntity.ok("Logged out successfully");
	    }
	
    
	//get user role after login
//		@RequestMapping(value="/get-userRole", method = RequestMethod.GET)
//		public ResponseEntity<Object> getRole(Principal principal){
//			try {				
//				Set<Role> role = adminservices.getRole(principal);
//				return ResponseEntity.ok(role);
//			} catch (Exception e) {
//				return new ResponseEntity<Object>(e,HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
	
		
	// register new user 
		@PostMapping("/register")
		public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRequest userDto){
			try {
				UserResponse registeredUser = this.adminservices.registerNewUser(userDto);
				return new ResponseEntity<Object>(registeredUser,HttpStatus.CREATED);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
	// update/'change password
		@PostMapping("/changePassword")
		public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePassKey password,Principal principal){
			try {
				UserResponse changePassword = this.adminservices.changeUserPassword(password,principal);
				return new ResponseEntity<Object>(changePassword,HttpStatus.OK);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}	
	
	// add department
		@RequestMapping(value="/add-department", method = RequestMethod.POST)
		public ResponseEntity<Object> addDepartment(@RequestBody DepartmentDto departmentDto){
			try {
				DepartmentDto createDepartment = adminservices.createDepartment(departmentDto);
				return new ResponseEntity<Object>(createDepartment,HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		
	// update department
		@RequestMapping(value="/update-department", method = RequestMethod.POST)
		public ResponseEntity<Object> updateDepartment(@RequestBody DepartmentDto departmentDto){
			try {
				departmentDto.setId(departmentDto.getId());
				DepartmentDto updateDepartment = adminservices.updateDepartment(departmentDto);
				return new ResponseEntity<Object>(updateDepartment,HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
				

	//get department list
		@RequestMapping(value="/get-department-list", method = RequestMethod.GET)
		public ResponseEntity<Object> getDepartmentList(){
			try {
				List<DepartmentDto> departmentList = this.adminservices.getDepartmentList();
				return ResponseEntity.ok(departmentList);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		@RequestMapping(value="/get-alldepartment-list", method = RequestMethod.GET)
		public ResponseEntity<Object> getAllDepartmentList(){
			try {
				List<DepartmentDto> departmentList = this.adminservices.getAllDepartmentList();
				return ResponseEntity.ok(departmentList);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		
	//add category
		@RequestMapping(value="/add-category", method = RequestMethod.POST)
		public ResponseEntity<Object> addCategory(@RequestBody CategoryDto categoryDto){
			try {
				CategoryDto createCategory = adminservices.addCategory(categoryDto);
				return new ResponseEntity<Object>(createCategory,HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
	// update category
		@RequestMapping(value="/update-category", method = RequestMethod.POST)
		public ResponseEntity<Object> updateCategoory(@RequestBody CategoryDto categoryDto){
			try {
				categoryDto.setId(categoryDto.getId());
				CategoryDto updateCategory = adminservices.updateCategory(categoryDto);
				return new ResponseEntity<Object>(updateCategory,HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
				
	//get category list
		@RequestMapping(value="/get-category-list", method = RequestMethod.GET)
		public ResponseEntity<Object> getCategoryList(){
			try {
				List<CategoryDto> categoryList = this.adminservices.getCategoryList();
				return ResponseEntity.ok(categoryList);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}	
		
		@RequestMapping(value="/get-allcategory-list", method = RequestMethod.GET)
		public ResponseEntity<Object> getAllCategoryList(){
			try {
				List<CategoryDto> categoryList = this.adminservices.getAllCategoryList();
				return ResponseEntity.ok(categoryList);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}	
		
	//assign category to department
		@RequestMapping(value="/assign-category-todepartment", method = RequestMethod.POST)
		public ResponseEntity<Object> assignCategory(@RequestBody ResponsibilitiesRequest responsibilitiesRequest){
			try {
				DepartmentDto assignedCategory = adminservices.assignCategoryToDepartment(responsibilitiesRequest.getDepartmentId(),responsibilitiesRequest.getCategoryId());
				return new ResponseEntity<Object>(assignedCategory,HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		@RequestMapping(value="/unassign-category-todepartment", method = RequestMethod.POST)
		public ResponseEntity<Object> unassignCategory(@RequestBody ResponsibilitiesRequest responsibilitiesRequest){
			try {
				DepartmentDto assignedCategory = adminservices.unassignCategoryToDepartment(responsibilitiesRequest.getDepartmentId(),responsibilitiesRequest.getCategoryId());
				return new ResponseEntity<Object>(assignedCategory,HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//to add user roles in role table
		@RequestMapping(value="/add-user-roles", method = RequestMethod.POST,consumes={"application/json"})
		public ResponseEntity<Object> addRole(@RequestBody RoleDto roleDto){
			try {
				RoleDto createRole = adminservices.createRole(roleDto);
				return new ResponseEntity<Object>(createRole,HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
	
		//get roles list from role table
		@RequestMapping(value="/get-roles", method = RequestMethod.GET)
		public ResponseEntity<Object> getRoles(){
			try {
				List<RoleDto> roles = adminservices.getRoles();
				return ResponseEntity.ok(roles);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}

		//PUT-update user
		@PostMapping("/update-user")
		public ResponseEntity<Object> updateUser(@Valid @RequestBody UserRequest userDto){
			try {
				UserResponse updatedUser = this.adminservices.updateUser(userDto, userDto.getEmployeeId());
				return ResponseEntity.ok(updatedUser);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//DELETE-delete user
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@PostMapping("/do-inactive-user")
		public ResponseEntity<ApiResponse> inactiveUser(@RequestBody EmpRequest empRequest){
			this.adminservices.inactiveUser(empRequest.getEmployeeId());
			return new ResponseEntity<ApiResponse>(new ApiResponse("User Inactive Successfully",true), HttpStatus.OK);
		}
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@PostMapping("/do-active-user")
		public ResponseEntity<ApiResponse> activeUser(@RequestBody EmpRequest empRequest){
			this.adminservices.activeUser(empRequest.getEmployeeId());
			return new ResponseEntity<ApiResponse>(new ApiResponse("User Active Successfully",true), HttpStatus.OK);
		}
		
		//GET-all users
		@GetMapping("/get-all-users")
		public ResponseEntity<Object> getAllUsers(){
			try {
				return ResponseEntity.ok(this.adminservices.getAllUsers());			
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//GET-user by empId
		@PostMapping("/get-userBy-empId")
		public ResponseEntity<Object> getSingleUser(@RequestBody EmpRequest empRequest){
			try {
				return ResponseEntity.ok(this.adminservices.getEmployeByEmpId(empRequest.getEmployeeId()));				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//Available category that can be assigned to help desk
		@PostMapping("/category-toassign")
		public ResponseEntity<Object> categogyCanBeAssign(@RequestBody CategoryAssign categoryAssign) {
			try {
				System.out.println("Line 327 : " + categoryAssign.getEmployeeId());
				Map<String,List<CategoryDto>> category = adminservices.categogyCanBeAssign(categoryAssign);
				return ResponseEntity.ok(category);							
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		} 
		
		//assign department->category to help desk
		@PostMapping("/assign-role-responsibilities")
		public ResponseEntity<Object> assignResponsibilities(@RequestBody ResponsibilitiesRequest responsibilitiesRequest) {
			try {
				System.out.println("Line 338 :");
				ResponsibilitiesResponse assignedResponsibilities = adminservices.assignResponsibilities(responsibilitiesRequest);
				return ResponseEntity.ok(assignedResponsibilities);					
			} catch (Exception e) {
				return new ResponseEntity<Object>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
			}
		} 
				
		@PostMapping("/unassign-role-responsibilities")
		public ResponseEntity<Object> unassignResponsibilities(@RequestBody ResponsibilitiesRequest responsibilitiesRequest) {
			try {
				ResponsibilitiesRequest assignedResponsibilities = adminservices.unassignResponsibilities(responsibilitiesRequest);
				return ResponseEntity.ok(assignedResponsibilities);					
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		} 
		
		//create grievance IOException
		@PostMapping(value= "/addGrievance",  produces = "application/json", consumes = "application/json")
		public ResponseEntity<Object> createGreivance(@Valid @RequestBody GrievenceRequest grievence,Principal principal) throws Exception
		{
			try {
				GrievenceResponse grievenceDtoResponse = this.adminservices.createGrievence(grievence,principal);
				return new ResponseEntity<Object>(grievenceDtoResponse,HttpStatus.CREATED);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//add grievance remarks
		@PostMapping(value= "/addRemarks")
		public ResponseEntity<Object> addRemarks(@Valid @RequestBody RemarksRequest remarksRequest,Principal principal) throws IOException
		{
			try {
				List<RemarksResponse> remarksResponse = this.adminservices.addRemarks(remarksRequest,principal);
				return new ResponseEntity<Object>(remarksResponse,HttpStatus.OK);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//check grievance status and send otp
		@PostMapping("/check-grievance-status")
		public ResponseEntity<Object> checkGrievanceStatus(@Valid @RequestBody GrievanceStatus grievanceStatus){
			try {
				GrievanceStatus checkGrievenceStatus = adminservices.checkGrievenceStatus(grievanceStatus);
				return new ResponseEntity<Object>(checkGrievenceStatus,HttpStatus.OK);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}	
		}
		
		//check grievance status after verifying otp
		@PostMapping("/verify-grievance-status")
		public ResponseEntity<Object> verifyGrievanceStatus(@Valid @RequestBody GrievanceStatus grievanceStatus){
				try {
					GrievanceStatus verifyGrievanceStatus = adminservices.verifyGrievenceStatus(grievanceStatus);
					return new ResponseEntity<Object>(verifyGrievanceStatus,HttpStatus.OK);					
				} catch (Exception e) {
					return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
				}
		}
		
		
		//check grievance status
//		@GetMapping("/check-grievance-status/{grievanceRef}")
//		public ResponseEntity<GrievenceResponse> checkGrievanceStatus(@PathVariable("grievanceRef") String grievanceRef){
//			GrievenceResponse checkGrievenceStatus = adminservices.checkGrievenceStatusByGrievanceRef(grievanceRef);
//			return new ResponseEntity<GrievenceResponse>(checkGrievenceStatus,HttpStatus.OK);
//		}
		
		
		//get grievance by employee Id
		@GetMapping("/GrievenceByEmpId")
		public ResponseEntity<Object> getCategory(Principal principal){
			try {
				String empId = principal.getName();	
				Map<String, List<GrievenceResponse>> grievenceByEmpId = adminservices.getGrievenceByEmpId(empId);
				return new ResponseEntity<Object>(grievenceByEmpId,HttpStatus.OK);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		
		//get grievance list department wise to helpdesk
		@GetMapping("/grievancesBy-department-wise")
		public ResponseEntity<Object> getGrievancesByDepartmentWise(Principal principal){
			try {
				Map<String, List<GrievenceResponse>> grievanceList = null;
				grievanceList = adminservices.getGrievancesByDepartmentWise(principal);
				if(grievanceList != null) {
					return new ResponseEntity<Object>(grievanceList,HttpStatus.OK);									
				}
				else {
					return ResponseEntity.ok(grievanceList);
				}
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//get grievance list department wise to staff
		@GetMapping("/grievancesToStaff")
		public ResponseEntity<Object> getGrievancesToStaff(Principal principal){
			try {
				Map<String, List<GrievenceResponse>> grievanceList = null;
				grievanceList = adminservices.getGrievancesToStaff(principal);
				if(grievanceList != null) {
					return new ResponseEntity<Object>(grievanceList,HttpStatus.OK);									
				}
				else {
					return ResponseEntity.ok(grievanceList);
				}
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//check grievance status
		@PostMapping("/can-assign-grievanceTo")
		public ResponseEntity<Object> canAssignGrievanceTo(@RequestBody ResponsibilitiesRequest responsibilitiesRequest){
			try {
				List<ResponsibilitiesResponse> users = adminservices.canAssignGrievanceTo(responsibilitiesRequest);
				return new ResponseEntity<Object>(users,HttpStatus.OK);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		//check grievance status
		@PostMapping("/assign-grievance")
		public ResponseEntity<Object> assignGrievance(@RequestBody AssignGrievance assignGrievance){
			try {
				GrievenceResponse assigned = adminservices.assignGrievance(assignGrievance.getGrievanceRefNo(), assignGrievance.getEmpId());
				return new ResponseEntity<Object>(assigned,HttpStatus.OK);				
			}catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		
		
		//check grievance status
		@PostMapping("/update-grievance-status")
		public ResponseEntity<Object> updateGrievanceStatus(@RequestBody RemarksRequest remarksRequest){  
			try {
				GrievenceResponse updated = adminservices.updateGrievanceStatus(remarksRequest.getGrievanceRefNo(), remarksRequest.getRemarks());
				return new ResponseEntity<Object>(updated,HttpStatus.OK);				
			} catch (Exception e) {
				return new ResponseEntity<Object>(e,HttpStatus.EXPECTATION_FAILED);
			}
		}
		

		

//------------not in used-----		
		//get empMasterList
		@RequestMapping(value="/get-employeeDetail", method = RequestMethod.POST)
		public ResponseEntity<Object> getEmployeeDetail(@Valid @RequestBody EmpMasterRequest empMasterRequest){
			try {
				
				int randomNumber = ApplicationUtility.generateOTP(6);
				String randomKey = ApplicationUtility.generatePassword(6);
	
				String uri = "http://localhost:8081/get-emp";
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<EmpMasterRequest> response = restTemplate.postForEntity(uri, empMasterRequest, EmpMasterRequest.class);
				EmpMasterRequest result = response.getBody();
				
				if(response.getBody() != null && response.getStatusCodeValue() == 200) {
				
		        	User user = adminservices.getUserByEmpId(response.getBody().getEmployeeId());
					if(user != null) {
		        		user.setOtp(randomNumber);
		        		user.setOtpSentOn(new Date());
		        		userRepo.save(user);		        	
		        	}
					else if(user == null) {
						adminservices.createNewUser(response.getBody(),randomNumber,randomKey);
		        	}	
					
					response.getBody().setOtp(randomNumber);
					return ResponseEntity.ok(result);
				}else{
					return new ResponseEntity<>("Employee not found",HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				return new ResponseEntity<Object>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}


			
		
}

//		class EncryptedData {
//		    private String data;
//		
//		    public String getData() {
//		        return data;
//		    }
//		
//		    public void setData(String data) {
//		        this.data = data;
//		    }
//		}
