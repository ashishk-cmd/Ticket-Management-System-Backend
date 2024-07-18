package aiims.cf.tms_api.services.impl;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import aiims.cf.tms_api.config.AppConstants;
import aiims.cf.tms_api.exceptions.AlreadyExistsException;
import aiims.cf.tms_api.exceptions.ResourceNotFoundException;
import aiims.cf.tms_api.models.Action;
import aiims.cf.tms_api.models.Category;
import aiims.cf.tms_api.models.Department;
import aiims.cf.tms_api.models.Grievence;
import aiims.cf.tms_api.models.Remarks;
import aiims.cf.tms_api.models.Responsibilities;
import aiims.cf.tms_api.models.Role;
import aiims.cf.tms_api.models.User;
import aiims.cf.tms_api.payload.requests.CategoryAssign;
import aiims.cf.tms_api.payload.requests.ChangePassKey;
import aiims.cf.tms_api.payload.requests.EmployeeRequest;
import aiims.cf.tms_api.payload.requests.GrievenceRequest;
import aiims.cf.tms_api.payload.requests.MyRequestBody;
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
import aiims.cf.tms_api.repositories.ActionRepo;
import aiims.cf.tms_api.repositories.CategoryRepo;
import aiims.cf.tms_api.repositories.DepartmentRepo;
import aiims.cf.tms_api.repositories.GrievenceRepo;
import aiims.cf.tms_api.repositories.RemarksRepo;
import aiims.cf.tms_api.repositories.ResponsibilitiesRepo;
import aiims.cf.tms_api.repositories.RoleRepo;
import aiims.cf.tms_api.repositories.UserRepo;
import aiims.cf.tms_api.services.AdminServices;
import aiims.cf.tms_api.utils.ApplicationUtility;
import aiims.cf.tms_api.utils.ImageUpload;
import aiims.cf.tms_api.utils.TextMessageServices;
import aiims.cf.tms_api.utils.TextMessageTemplate;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class AdminServiceImpl implements AdminServices {

	 
	@Autowired
	private DepartmentRepo departmentRepository;
	@Autowired
	private CategoryRepo categoryRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private RoleRepo roleRepository;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private GrievenceRepo grievenceRepo;
	@Autowired
	private ResponsibilitiesRepo responsibilitiesRepo;
	@Autowired
	private ActionRepo actionRepo;
	@Autowired
	private RemarksRepo remarksRepo	;
	@Autowired
    private RestTemplate restTemplate;
	@Autowired
	private TextMessageServices textMessageServices;
	
	
	@Value("${file.path}")
	private String saveLocation;
	
	@Value("${employee.path}")
	private boolean employeePath;
	
	@Value("${application.textmessage.active}")
	private boolean sendMessage ;
	
	
//	@Override
//	public Set<Role> getRole(Principal principal) {
//		Set<Role> userRole = null;
//		String empId = principal.getName();
//        User user = userRepo.findByEmployeeId(empId).orElse(null);
//        if(user != null) {
//        	userRole = user.getRoles();
//        }else {
//        	userRole = null;
//        }
//        return userRole;        	
//	}
	
	@Override
	public Set<Role> getRole(User user) {
		Set<Role> userRole = null;
        if(user != null) {
        	userRole = user.getRoles();
        }else {
        	userRole = null;
        }
        return userRole;      
	}
	
	@Override
	public List<RoleDto> getRoles() {
		List<Role> role = roleRepository.findAll();
		List<RoleDto> roledtos = role.stream().map((r)-> 
		this.modelMapper.map(r,RoleDto.class)).collect(Collectors.toList());
		return roledtos;
	}

//-----Department and Category-----------------------------------------------------------------------------------------------------------	
	
	@Override
	public DepartmentDto createDepartment(DepartmentDto departmentDto) {
		Department existingDepartment = departmentRepository.findByName(departmentDto.getName()).orElse(null);
		if(existingDepartment == null) {
			Department department = this.modelMapper.map(departmentDto, Department.class);
			department.setStatus(AppConstants.ACTIVE);
			Department savedDepartment = this.departmentRepository.save(department);
			return this.modelMapper.map(savedDepartment, DepartmentDto.class);
		}else {
			throw new AlreadyExistsException("Department", "Department name", departmentDto.getName());
		}
	}
	
	
	@Override
	public DepartmentDto updateDepartment(DepartmentDto departmentDto) {
		Department existingDepartment = departmentRepository.findById(departmentDto.getId()).orElse(null);
		if(existingDepartment != null) {
			existingDepartment.setName(departmentDto.getName());
			Department savedDepartment = this.departmentRepository.save(existingDepartment);
			return this.modelMapper.map(savedDepartment, DepartmentDto.class);
		}else {
			throw new ResourceNotFoundException("Department", "Department Id", departmentDto.getId());
		}
	}
	

	@Override
	public List<DepartmentDto> getDepartmentList() {
		List<Department> departments = this.departmentRepository.findAllByStatus(AppConstants.ACTIVE);
		List<DepartmentDto> deptdtos = departments.stream().map((dept)-> this.modelMapper.map(dept,DepartmentDto.class)).collect(Collectors.toList());
		return deptdtos;
	}

	@Override
	public List<DepartmentDto> getAllDepartmentList() {
		List<Department> departments = this.departmentRepository.findAll();
		List<DepartmentDto> deptdtos = departments.stream().map((dept)-> this.modelMapper.map(dept,DepartmentDto.class)).collect(Collectors.toList());
		return deptdtos;
	}
	
	@Override
	public CategoryDto addCategory(CategoryDto categoryDto) {
		Category existingCategory = categoryRepository.findByName(categoryDto.getName()).orElse(null);
		if(existingCategory == null) {
			Category category = this.modelMapper.map(categoryDto, Category.class);
			category.setStatus(AppConstants.ACTIVE);
			Category savedCategory = this.categoryRepository.save(category);
			return this.modelMapper.map(savedCategory, CategoryDto.class);
		}else {
			throw new AlreadyExistsException("Category", "Category name", categoryDto.getName());
		}
	}
	
	
	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto) {
		Category existingCategory = categoryRepository.findById(categoryDto.getId()).orElse(null);
		if(existingCategory != null) {
			existingCategory.setName(categoryDto.getName());
			Category savedCategory = this.categoryRepository.save(existingCategory);
			return this.modelMapper.map(savedCategory, CategoryDto.class);
		}else {
			throw new ResourceNotFoundException("Category", "Category name", categoryDto.getId());
		}
	}
	
	
	@Override
	public List<CategoryDto> getCategoryList() {
		List<Category> category = this.categoryRepository.findAllByStatus(AppConstants.ACTIVE);
		List<CategoryDto> categorydtos = category.stream().map((cat)-> this.modelMapper.map(cat,CategoryDto.class)).collect(Collectors.toList());
		return categorydtos;
	}
	
	@Override
	public List<CategoryDto> getAllCategoryList() {
		List<Category> category = this.categoryRepository.findAll();
		List<CategoryDto> categorydtos = category.stream().map((cat)-> this.modelMapper.map(cat,CategoryDto.class)).collect(Collectors.toList());
		return categorydtos;
	}

	@Override
	public DepartmentDto assignCategoryToDepartment(Long departmentId, Long categoryId) {
		Department department = departmentRepository.findById(departmentId).orElseThrow(()-> new ResourceNotFoundException("Department","Department ID",departmentId));
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","Category Id",categoryId));
		List<Category> currentCategories = department.getCategories();
		 		
		currentCategories.add(category);
		department.setCategories(currentCategories);
		Department savedDepartment = departmentRepository.save(department);		
		
		return this.modelMapper.map(savedDepartment, DepartmentDto.class);
	}
	
	@Override
	public DepartmentDto unassignCategoryToDepartment(Long departmentId, Long categoryId) {
		Department department = departmentRepository.findById(departmentId).orElseThrow(()-> new ResourceNotFoundException("Department","Department ID",departmentId));
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","Category Id",categoryId));
		List<Category> currentCategories = department.getCategories();
		
		currentCategories.remove(category);
		department.setCategories(currentCategories);
		Department savedDepartment = departmentRepository.save(department);		
		
		return this.modelMapper.map(savedDepartment, DepartmentDto.class);
	}

	
//	@Override
//	public Map<String, List<CategoryDto>> categogyCanBeAssign(CategoryAssign categoryAssign) {
//		Department department = departmentRepository.findById(categoryAssign.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("Department","Department ID",categoryAssign.getDepartmentId()));		
//		System.out.println("Line 264 : " + categoryAssign.getEmployeeId().toUpperCase());
//		User user = userRepo.findByEmployeeId(categoryAssign.getEmployeeId().toUpperCase()).orElseThrow(()->new ResourceNotFoundException("Employee", " Employee Id ", categoryAssign.getEmployeeId()));
//				
//		List<Responsibilities> existingResponsibilities = responsibilitiesRepo.findByDepartmentAndUser(department,user);
//		System.out.println(existingResponsibilities.toString());
//		List<Category> categoriesList = department.getCategories();
//		List<CategoryDto> categorydtos = categoriesList.stream().map((cat)-> this.modelMapper.map(cat,CategoryDto.class)).collect(Collectors.toList());
//		List<CategoryDto> activeCategoriesDto = categorydtos.stream().filter(c -> c.getStatus().equalsIgnoreCase("active") && existingResponsibilities.contains(c)).map(cat -> cat).collect(Collectors.toList());
//		// Create a list to hold categories to be removed
//		List<CategoryDto> categoriesToRemove = new ArrayList<>();
//		
////		if(existingResponsibilities!=null && categorydtos!=null) {
////			for (CategoryDto cat : categorydtos) {
////				for (Responsibilities res : existingResponsibilities) {
////					if (cat.getId() == res.getCategory().getId() && res.getStatus().equalsIgnoreCase("ACTIVE")) {
////						categoriesToRemove.add(cat);
////						break;
////					}
////				}
////			}			
////		}
//
//		// Remove categories from categoriesList
//		categorydtos.removeAll(categoriesToRemove);
//		
//		Map<String, List<CategoryDto>> categorymap = new HashMap<String,List<CategoryDto>>();          
//		categorymap.put(AppConstants.NOTASSIGNED, categorydtos);
//		categorymap.put(AppConstants.ASSIGNED, activeCategoriesDto);
//			
//		return categorymap;
//	}
	
	
	@Override
	public Map<String, List<CategoryDto>> categogyCanBeAssign(CategoryAssign categoryAssign) {
	    Department department = departmentRepository.findById(categoryAssign.getDepartmentId())
	            .orElseThrow(() -> new ResourceNotFoundException("Department", "Department ID", categoryAssign.getDepartmentId()));

	    User user = userRepo.findByEmployeeId(categoryAssign.getEmployeeId().toUpperCase())
	            .orElseThrow(() -> new ResourceNotFoundException("Employee", "Employee Id", categoryAssign.getEmployeeId()));

	    List<Responsibilities> existingResponsibilities = responsibilitiesRepo.findByDepartmentAndUser(department, user);
	    List<Category> categoriesList = department.getCategories();
	    List<CategoryDto> categoryDtos = categoriesList.stream()
	            .map(cat -> this.modelMapper.map(cat, CategoryDto.class))
	            .collect(Collectors.toList());

	    List<CategoryDto> activeCategoriesDto = new ArrayList<>();
	    List<CategoryDto> inactiveCategoriesDto = new ArrayList<>();

	    if (existingResponsibilities != null && categoryDtos != null) {
	        for (Responsibilities res : existingResponsibilities) {
	            CategoryDto categoryDto = this.modelMapper.map(res.getCategory(), CategoryDto.class);
	            if (res.getStatus().equalsIgnoreCase("ACTIVE")) {
	                activeCategoriesDto.add(categoryDto);
	            } else {
	                inactiveCategoriesDto.add(categoryDto);
	            }
	        }
	    }

	    // Remove active and inactive categories from the categoryDtos list to get unassigned categories
	    categoryDtos.removeAll(activeCategoriesDto);
	    categoryDtos.removeAll(inactiveCategoriesDto);

	    Map<String, List<CategoryDto>> categoryMap = new HashMap<>();
	    categoryMap.put(AppConstants.NOTASSIGNED, categoryDtos);
	    categoryMap.put(AppConstants.ASSIGNED, activeCategoriesDto);
	    categoryMap.put(AppConstants.INACTIVE, inactiveCategoriesDto); // Add this line to include inactive categories

	    return categoryMap;
	}

	
	@Override
	public ResponsibilitiesResponse assignResponsibilities(ResponsibilitiesRequest responsibilitiesRequest) {
		Responsibilities saved = new Responsibilities();
		System.out.println("Line 299 " + responsibilitiesRequest.getUserId().toUpperCase());
		User user = userRepo.findByEmployeeId(responsibilitiesRequest.getUserId().toUpperCase()).orElseThrow(()-> new ResourceNotFoundException("Employee", " Employee Id ", responsibilitiesRequest.getUserId()));
		System.out.println("Line 302 " + user.toString());
		Department department = departmentRepository.findById(responsibilitiesRequest.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("Department","Department ID",responsibilitiesRequest.getDepartmentId()));
		System.out.println("Line 304 " + department.toString());
		Category category = categoryRepository.findById(responsibilitiesRequest.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category","Category ID",responsibilitiesRequest.getCategoryId()));
		System.out.println("Line 306 " + category);
		List<Responsibilities> existingResponsibilities = responsibilitiesRepo.findByDepartmentAndCategoryAndUserAndStatus(department,category,user,"ACTIVE");
		System.out.println("existingResponsibilities : "+existingResponsibilities.toString());
		if(existingResponsibilities.isEmpty()) {
			System.out.println("**************");
				Responsibilities responsibilities = new Responsibilities();	
				responsibilities.setUser(user);
				responsibilities.setDepartment(department);
				responsibilities.setCategory(category);
				responsibilities.setStatus(AppConstants.ACTIVE);
				saved = responsibilitiesRepo.save(responsibilities);
		}
		else {
			throw new AlreadyExistsException("Employee : " + user.getEmployeeId(), "Department : "+department.getName(),"Category : "+ category.getName());
		}
		ResponsibilitiesResponse mapResponsibilitiesResponse = this.modelMapper.map(saved, ResponsibilitiesResponse.class);
			
		List<Responsibilities> assignedResponsibilities = responsibilitiesRepo.findByDepartmentAndUserAndStatus(department,user,"ACTIVE");
		List<Category> assignedCategory = new ArrayList<>();
			for(Responsibilities r:assignedResponsibilities) {
				assignedCategory.add(r.getCategory());
			}
			mapResponsibilitiesResponse.setAssignedCategory(assignedCategory);
			mapResponsibilitiesResponse.setDepartment(department);
			mapResponsibilitiesResponse.getUser().setPassword(null);
		return mapResponsibilitiesResponse;
	}
	
	@Override
	public ResponsibilitiesRequest unassignResponsibilities(ResponsibilitiesRequest responsibilitiesRequest) {
		Responsibilities saved = null;
		User user = userRepo.findByEmployeeId(responsibilitiesRequest.getUserId().toUpperCase()).orElseThrow(()->new ResourceNotFoundException("Employee", " Employee Id ", responsibilitiesRequest.getUserId()));
		Department department = departmentRepository.findById(responsibilitiesRequest.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("Department","Department ID",responsibilitiesRequest.getDepartmentId()));
		Category category = categoryRepository.findById(responsibilitiesRequest.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category","Category ID",responsibilitiesRequest.getCategoryId()));
		List<Responsibilities> existingResponsibilities = responsibilitiesRepo.findByDepartmentAndCategoryAndUser(department,category,user);

		if(!existingResponsibilities.isEmpty() && existingResponsibilities.size() > 0) {
				Responsibilities responsibilities = existingResponsibilities.get(0);	
				responsibilities.setStatus(AppConstants.INACTIVE);
				saved = responsibilitiesRepo.save(responsibilities);	
		}
		else {
			throw new ResourceNotFoundException("Employee : " + user.getEmployeeId(), "Department : "+department.getName(),"Category : "+ category.getName());
		}
		return this.modelMapper.map(saved, ResponsibilitiesRequest.class);
	}

//-----Roles And User-----------------------------------------------------------------------------------------------------------		
	
	@Override
	public RoleDto createRole(RoleDto roleDto) {
		Role existingRole = roleRepository.findByName(roleDto.getName()).orElse(null);
		if(existingRole == null) {
			Role role = this.modelMapper.map(roleDto, Role.class);
			Role savedRole = this.roleRepository.save(role);
			return this.modelMapper.map(savedRole, RoleDto.class);
		}else {
			throw new AlreadyExistsException("Role", "Role name", roleDto.getName());
		}
	}
	

	@Override
	public UserResponse registerNewUser(UserRequest userRequest) throws Exception {
		userRequest.setEmployeeId(userRequest.getEmployeeId().toUpperCase());
		System.out.println(userRequest.getEmployeeId());
		User existingUserEmpId = userRepo.findByEmployeeId(userRequest.getEmployeeId().toUpperCase()).orElse(null);
		User existingUserContactNo = userRepo.findByContactNo(userRequest.getContactNo()).orElse(null);
		User existingUserEmail = userRepo.findByEmail(userRequest.getEmail()).orElse(null);
		Role existingRole = roleRepository.findById(userRequest.getRoleId()).orElse(null);
		
		
		if(existingUserEmpId == null && existingUserContactNo == null && existingUserEmail == null) {
			User user = this.modelMapper.map(userRequest, User.class);
			//status
			user.setStatus(AppConstants.NEW_USER_STATUS);
			
			Department department = new Department();
			if(userRequest.getDepartmentId() != null) {
				department = departmentRepository.findById(userRequest.getDepartmentId()).orElse(null);				
			}
			if(existingRole.getName().equalsIgnoreCase(AppConstants.ADMIN_USER)) {
				User userAdmin = userRepo.findUsersWithRoles(existingRole.getName()).orElse(null);
				if(userAdmin != null) {
					throw new AlreadyExistsException("Employee", "Role", "Admin");
				}
			}
			else if(userRequest.getDepartmentId() != null && existingRole.getName().equalsIgnoreCase(AppConstants.HELPDESK_USER)) {
				User existingUserDepartment = userRepo.findByDepartment(department).orElse(null);
				if(existingUserDepartment != null)
					throw new AlreadyExistsException("Employee with Department : " + department.getName(), "Role", "Helpdesk");
			}
			else
				department = null; 
				user.setDepartment(department);	
			
			//enabled user
			user.setEnabled(true);
			//encoded the password
			String randomKey = ApplicationUtility.generatePassword(6);
			user.setPassword(this.passwordEncoder.encode(randomKey));
			//registered on
			user.setRegisteredOn(new Date());
			
			//roles
			if(userRequest.getRoles() == null) {
				Role role = this.roleRepository.findByName(AppConstants.NORMAL_USER).get();	
				user.getRoles().add(role);				
			}
			if(userRequest.getRoles() != null) {
				
				if(existingRole != null) {
					Role role = roleRepository.findByName(existingRole.getName()).get();	
					user.getRoles().add(role);								
				}
			}
			
			UserResponse mapUser = new UserResponse();
			User saveduser = this.userRepo.save(user);
			 if(saveduser != null) {
				 mapUser = this.modelMapper.map(saveduser, UserResponse.class);
					if(sendMessage) {
						textMessageServices.sendMessage(saveduser.getContactNo(),
								"Your account has been successfully registered." +
        						" Your UserId is:" + saveduser.getEmployeeId() +
        						" and Your temporary password is: " + randomKey + 
        						".Please log in and change your password. Thank you for joining us!");
						mapUser.setPassword(null);
					}else {
						mapUser.setPassword(randomKey);						
					}
				}
		   return mapUser;
		}
		if(existingUserEmpId != null) {
			throw new AlreadyExistsException("Employee", "Employee Id", userRequest.getEmployeeId());
		}
		if(existingUserContactNo != null) {
			throw new AlreadyExistsException("Contact", "Contact No.", userRequest.getContactNo());
		}
		if(existingUserEmail != null) {
			throw new AlreadyExistsException("Email", "Email Id", userRequest.getEmail());
		}
		
		return this.modelMapper.map(userRequest, UserResponse.class);
	}

	
	@Override
	public UserResponse updateUser(UserRequest userDto, String empId) {
		User user = this.userRepo.findByEmployeeId(empId.toUpperCase()).orElseThrow(()-> new ResourceNotFoundException("User"," id ",empId));	
			user.setFullName(userDto.getFullName());
			//if option not given on front end than not required
//			user.setEmployeeId(user.getEmployeeId());
//			user.setStatus(user.getStatus());
//			user.setEnabled(user.getEnabled());
//			user.setPassword(user.getPassword());
			
//			Department department = departmentRepository.findById(userDto.getDepartmentId()).orElse(null);
//			if(department !=null) {
//				user.setDepartment(department);				
//			}
			user.setEmail(userDto.getEmail());
			user.setContactNo(userDto.getContactNo());
//			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			UserResponse ur = new UserResponse();
			User updatedUser = this.userRepo.save(user);
			ur = this.modelMapper.map(updatedUser, UserResponse.class);
			ur.setPassword(null);
			return this.modelMapper.map(updatedUser, UserResponse.class);
	}

	
	@Override
	public User getUserByEmpId(String employeeId) {
		User user = this.userRepo.findByEmployeeId(employeeId.toUpperCase()).orElse(null);
		return user;
	}
	

	@Override
	public UserResponse getEmployeByEmpId(String employeeId) {
		User user = this.userRepo.findByEmployeeId(employeeId.toUpperCase()).orElseThrow(()-> new ResourceNotFoundException("Employee"," Id ",employeeId));
		return this.modelMapper.map(user, UserResponse.class);
	}

	
	@Override
	public List<UserResponse> getAllUsers() {
		List<User> users = this.userRepo.findUsersWithRolesOtherThanUser(AppConstants.NORMAL_USER);		
		List<UserResponse> userDto = users.stream().map((user)->this.modelMapper.map(user, UserResponse.class)).collect(Collectors.toList());
		return userDto;
	}

	
	@Override
	public void inactiveUser(String empId) {
		User user = this.userRepo.findByEmployeeId(empId.toUpperCase()).orElseThrow(()-> new ResourceNotFoundException("Employee"," Id ",empId));
		user.setStatus(AppConstants.INACTIVE_USER_STATUS);
		user.setEnabled(false);
		this.userRepo.save(user);
	}

	
	@Override
	public void activeUser(String empId) {
		User user = this.userRepo.findByEmployeeId(empId.toUpperCase()).orElseThrow(()-> new ResourceNotFoundException("Employee"," Id ",empId));
		user.setStatus(AppConstants.ACTIVE_USER_STATUS);
		user.setEnabled(true);
		this.userRepo.save(user);
	}

	
//-----Grievance-----------------------------------------------------------------------------------------------------------		
	
	@Override
	public GrievenceResponse createGrievence(GrievenceRequest grievenceRequest,Principal principal) throws Exception {
			Grievence savedGrievance = null;
			//if user is null
			if(principal == null) {	
				//check if user exists or not
				User existingUser = userRepo.findByEmployeeId(grievenceRequest.getEmployeeId().toUpperCase()).orElse(null);
				if(existingUser.getOtp() == grievenceRequest.getOtp() && grievenceRequest.getOtp() != 0  && existingUser != null) 
				{   
					savedGrievance = saveGrievance(grievenceRequest,existingUser);				
				}else {
					throw new ResourceNotFoundException("Employee/OTP", "Credentials", "not matched.");
				}
				existingUser.setOtp(0);
				existingUser.setOtpSentOn(null);
				userRepo.save(existingUser);	
			}
			
			//if user is not null
			if(principal != null) {
				String empId = principal.getName();
				User principalUser = userRepo.findByEmployeeId(empId.toUpperCase()).orElse(null);
				if(principalUser != null && grievenceRequest.getOtp() == 123456 ) {
					savedGrievance = saveGrievance(grievenceRequest,principalUser);
				}
				principalUser.setOtp(0);
				principalUser.setOtpSentOn(null);
				userRepo.save(principalUser);
			}	
			GrievenceResponse grievenceResponse = this.modelMapper.map(savedGrievance, GrievenceResponse.class);
			grievenceResponse.getCurrentUser().setPassword(null);
			grievenceResponse.getUserRaisedBy().setPassword(null);
		return this.modelMapper.map(grievenceResponse, GrievenceResponse.class);
	}

	
	//common method to save grievances
	private Grievence saveGrievance(GrievenceRequest grievenceRequest, User user) throws Exception {
		DateFormat dateformat = new SimpleDateFormat("yyMMddhhmm");  
		String strdate = dateformat.format(new Date());
		String fileName = "image_";
        
		Department department = departmentRepository.findById(grievenceRequest.getDepartmentId()).orElseThrow(()->new ResourceNotFoundException("Department", " Department Id ", grievenceRequest.getDepartmentId()));
		
		Category category = categoryRepository.findById(grievenceRequest.getCategoryId()).orElseThrow(()->new ResourceNotFoundException("Category", " Category Id ", grievenceRequest.getCategoryId()));
			
		Grievence savedGrievence = new Grievence();
		
		Grievence grievence = new Grievence();
		//setting grievance and saving 
		grievence.setConcern(grievenceRequest.getConcern());
		if(department != null) {grievence.setDepartment(department);}
		if(category != null) {grievence.setCategory(category);}
		grievence.setLastUpdate(new Date());
		grievence.setGrievanceStatus(AppConstants.NEW_GRIEVANCE_STATUS);
		
		User curuser  = userRepo.findUsersWithRolesAndDeparment(AppConstants.HELPDESK_USER,grievenceRequest.getDepartmentId()).orElse(null);
			if(curuser != null) {
				grievence.setCurrentUser(curuser);			
			}			
		grievence.setRaisedOn(new Date());
		grievence.setUserRaisedBy(user);
		savedGrievence = grievenceRepo.save(grievence);
		
		String existingId = String.format("%04d", savedGrievence.getId());
		
		if(savedGrievence != null)
		savedGrievence.setRefNo(strdate + existingId);
		
		if(grievenceRequest.getImage1()!=null) {
			boolean Image1 = ImageUpload.saveBase64Image(grievenceRequest.getImage1(), saveLocation, fileName + "1_" + strdate + existingId + ".jpg");
			if(Image1 == true)
			savedGrievence.setImage1(fileName + "1_" + strdate + existingId + ".jpg");					
		}

		if(grievenceRequest.getImage2()!=null)
		{
			boolean Image2 = ImageUpload.saveBase64Image(grievenceRequest.getImage2(), saveLocation, fileName + "2_" + strdate + existingId + ".jpg");
			if(Image2 == true)
			savedGrievence.setImage2(fileName + "2_" + strdate + existingId + ".jpg");
		}
		
		//updating ref no of grievance saved
		Grievence saved = grievenceRepo.save(savedGrievence);
		if(saved !=null) {
			if(sendMessage) {
				textMessageServices.sendMessage(user.getContactNo(),"Your complaint has been successfully registered. " + saved.getRefNo() + " is the refrence number to check your grievance status.");					
			}else {
				System.out.println(user.getContactNo() + " Your complaint has been successfully registered. " + saved.getRefNo() + " is the refrence number to check your grievance status.");
			}
		}
		
		return saved;
		
	}


	@Override
	public GrievanceStatus checkGrievenceStatus(GrievanceStatus grievanceStatus) throws Exception {
		User user = userRepo.findByEmployeeId(grievanceStatus.getEmployeeId().toUpperCase()).orElse(null);
		if(user != null) {
			Grievence grievence = grievenceRepo.findByUserRaisedByAndRefNo(user,grievanceStatus.getGrievanceRef());
			if(grievence != null) {
				int randomNumber = ApplicationUtility.generateOTP(6);
				grievence.setOtp(randomNumber);
				grievence.setOtpsenton(new Date());
				Grievence savedOtp = grievenceRepo.save(grievence);
				if(savedOtp != null) {
					if(sendMessage) {
						textMessageServices.sendMessage(user.getContactNo(),TextMessageTemplate.sendOTP(randomNumber,"check your grievance status with Grievance Id " + grievanceStatus.getGrievanceRef()));						
					}else {
						grievanceStatus.setOtp(randomNumber);						
					}
				}
				return this.modelMapper.map(grievanceStatus, GrievanceStatus.class);
			}else {
				throw new ResourceNotFoundException("Grievence","Grievence Refrence No.",grievanceStatus.getGrievanceRef());
			}	
		}else {
			throw new ResourceNotFoundException("Employee","Employee Id.",grievanceStatus.getEmployeeId());
		}
	}
	
	
	@Override
	public GrievanceStatus verifyGrievenceStatus(GrievanceStatus grievanceStatus) {
		User user = userRepo.findByEmployeeId(grievanceStatus.getEmployeeId().toUpperCase()).orElse(null);
		Grievence grievence = grievenceRepo.findByUserRaisedByAndRefNo(user,grievanceStatus.getGrievanceRef());
		if(grievence != null) {
			if(grievence.getOtp() == grievanceStatus.getOtp() && grievanceStatus.getOtp() != 0) {
				if(!(grievence.getGrievanceStatus().equalsIgnoreCase(AppConstants.CLOSED_GRIEVANCE_STATUS))) {
					grievanceStatus.setStatus(AppConstants.ACTIVE_GRIEVANCE_STATUS);
					grievence.setOtp(0);
					grievenceRepo.save(grievence);
				}
//				grievanceStatus.setStatus(grievence.getGrievanceStatus());
				return this.modelMapper.map(grievanceStatus, GrievanceStatus.class);				
			}else {
				throw new ResourceNotFoundException("OTP","OTP","NOT MATCHED");		
			}
		}else {
			throw new ResourceNotFoundException("Grievence","Grievence Refrence No.",grievanceStatus.getGrievanceRef());
		}
	}

	
	@Override
	public GrievenceResponse checkGrievenceStatusByGrievanceRef(String grievanceRef) {
		Grievence grievence = grievenceRepo.findByRefNo(grievanceRef);
		if(grievence != null) {
			return this.modelMapper.map(grievence, GrievenceResponse.class);
		}else {
			throw new ResourceNotFoundException("Grievence","Grievence Refrence No.",grievanceRef);
		}
	}

	
	
	

	private void saveActionPerfomed(String forwardedGrievanceStatus, User user, Grievence grievence) {
		Action action = new Action();
		action.setName(forwardedGrievanceStatus);
		action.setCdt(new Date());
		action.setUser(user);
		action.setGrievence(grievence);
		actionRepo.save(action);
	}

	
	@Override
	public GrievenceResponse updateGrievanceStatus(String grievanceRefNo,String status) {
		Grievence updatedGrievence = null;
		Grievence grievence = grievenceRepo.findByRefNo(grievanceRefNo);
		if(grievence != null) {
			grievence.setGrievanceStatus(status);
			updatedGrievence = grievenceRepo.save(grievence);
			
			saveActionPerfomed(status, grievence.getCurrentUser(), grievence);
		}else {
			throw new ResourceNotFoundException("Grievence", " Grievence Refrence number ", grievanceRefNo);
		}
		return this.modelMapper.map(updatedGrievence, GrievenceResponse.class);
	}

	
	@Override
	public Map<String, List<GrievenceResponse>> getGrievenceByEmpId(String empId) {	
		Map<String, List<GrievenceResponse>> gResponse = null;
		
		try {
			User user = userRepo.findByEmployeeId(empId.toUpperCase()).orElse(null);
			if(user != null) {
				List<Grievence> findByUserRaisedBy = grievenceRepo.findByUserRaisedBy(user);
				List<GrievenceResponse> grievenceResponse = findByUserRaisedBy.stream().map((griev)-> this.modelMapper.map(griev, GrievenceResponse.class)).collect(Collectors.toList());				
				
				for(GrievenceResponse g : grievenceResponse) {
					if(!(g.getGrievanceStatus().equalsIgnoreCase(AppConstants.CLOSED_GRIEVANCE_STATUS)) && !(g.getGrievanceStatus().equalsIgnoreCase(AppConstants.NEW_GRIEVANCE_STATUS))) {
						g.setGrievanceStatus(AppConstants.ACTIVE_GRIEVANCE_STATUS);
					}
					Grievence grievence = grievenceRepo.findByRefNo(g.getRefNo());
					List<Remarks> grievanceRemarks = remarksRepo.findByGrievence(grievence);
					List<RemarksResponse> remarksRes = grievanceRemarks.stream().map((GR)-> this.modelMapper.map(GR, RemarksResponse.class)).collect(Collectors.toList());				
					g.setRemarksResponse(remarksRes);
				}
	     		gResponse = grievenceResponse.stream().collect(Collectors.groupingBy(GrievenceResponse::getGrievanceStatus));
					
		}else {
			throw new ResourceNotFoundException("Employee","Employee Id" ,empId);
		}	
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gResponse;
	}
	
	
	@Override
	public Map<String, List<GrievenceResponse>> getGrievancesByDepartmentWise(Principal principal) {
		Map<String, List<GrievenceResponse>> gResponse = null;
		String empId = principal.getName();
		User user = userRepo.findByEmployeeId(empId.toUpperCase()).orElse(null);
		
			List<GrievenceResponse> grievenceResponse = null;
			
				List<Grievence> grievenceList = grievenceRepo.findByDepartment(user.getDepartment());				
				grievenceResponse = grievenceList.stream().map((griev)-> this.modelMapper.map(griev, GrievenceResponse.class)).collect(Collectors.toList());				
				
				for(GrievenceResponse g : grievenceResponse) {
					if(!(g.getGrievanceStatus().equalsIgnoreCase(AppConstants.CLOSED_GRIEVANCE_STATUS)) && !(g.getGrievanceStatus().equalsIgnoreCase(AppConstants.NEW_GRIEVANCE_STATUS))) {
						g.setGrievanceStatus(AppConstants.ACTIVE_GRIEVANCE_STATUS);
					}
					Grievence grievence = grievenceRepo.findByRefNo(g.getRefNo());
					List<Remarks> grievanceRemarks = remarksRepo.findByGrievence(grievence);
					List<RemarksResponse> remarksRes = grievanceRemarks.stream().map((GR)-> this.modelMapper.map(GR, RemarksResponse.class)).collect(Collectors.toList());				
					g.setRemarksResponse(remarksRes);
				}				
				grievenceResponse.addAll(grievenceResponse);
			
			gResponse = grievenceResponse.stream().collect(Collectors.groupingBy(GrievenceResponse::getGrievanceStatus));
						
		return gResponse;
	}
	
	
	@Override
	public Map<String, List<GrievenceResponse>> getGrievancesToStaff(Principal principal) {
		String empId = principal.getName();
		User user = userRepo.findByEmployeeId(empId.toUpperCase()).orElse(null);
		
		List<GrievenceResponse> grievenceRes = new ArrayList<>();
		
			List<Grievence> grievenceList = grievenceRepo.findByCurrentUserAndDepartment(user, user.getDepartment());				
			List<GrievenceResponse> grievenceResponse = grievenceList.stream().map((griev)-> this.modelMapper.map(griev, GrievenceResponse.class)).collect(Collectors.toList());				
			
			for(GrievenceResponse g : grievenceResponse) {
				if(!(g.getGrievanceStatus().equalsIgnoreCase(AppConstants.CLOSED_GRIEVANCE_STATUS)) && !(g.getGrievanceStatus().equalsIgnoreCase(AppConstants.NEW_GRIEVANCE_STATUS))) {
					g.setGrievanceStatus(AppConstants.ACTIVE_GRIEVANCE_STATUS);
				}
				Grievence grievence = grievenceRepo.findByRefNo(g.getRefNo());
				List<Remarks> grievanceRemarks = remarksRepo.findByGrievence(grievence);
				List<RemarksResponse> remarksRes = grievanceRemarks.stream().map((GR)-> this.modelMapper.map(GR, RemarksResponse.class)).collect(Collectors.toList());				
				g.setRemarksResponse(remarksRes);
			}				
			grievenceRes.addAll(grievenceResponse);
		
			Map<String, List<GrievenceResponse>> gResponse = grievenceResponse.stream().collect(Collectors.groupingBy(GrievenceResponse::getGrievanceStatus));
				
	return gResponse;
	}

		
	@Override
	public List<RemarksResponse> addRemarks(RemarksRequest remarksRequest,Principal principal) {
		String empid = principal.getName();
		User user = userRepo.findByEmployeeId(empid.toUpperCase()).orElse(null);
		Remarks savedremarks = null;
		List<RemarksResponse> remarksResponse = null;
		if(user != null)
		{		
			if(remarksRequest.getGrievanceRefNo() != null && remarksRequest.getRemarks() != null) {
				Grievence grievence = grievenceRepo.findByRefNo(remarksRequest.getGrievanceRefNo());
				if(grievence != null) {
					Remarks remarks = new Remarks();
					
					remarks.setGrievence(grievence);
					remarks.setRemarks(remarksRequest.getRemarks());
					
					if(grievence.getUserRaisedBy() == user) {
						remarks.setFromUser(user);
						remarks.setToUser(grievence.getCurrentUser());											
					}else if(grievence.getUserRaisedBy() != user){
						remarks.setFromUser(grievence.getCurrentUser());
						remarks.setToUser(grievence.getUserRaisedBy());																	
					}
					
					savedremarks = remarksRepo.save(remarks);
					if(savedremarks != null) {
						List<Remarks> grievanceRemarks = remarksRepo.findByGrievence(grievence);
						if(grievanceRemarks != null) {
							List<RemarksResponse> remarksRes = grievanceRemarks.stream().map((GR)-> this.modelMapper.map(GR, RemarksResponse.class)).collect(Collectors.toList());				
							remarksResponse =  remarksRes;
						}
					}
				}
			}
		}else {
			remarksResponse = null;			
		}
		
		return remarksResponse;
	}
	
	
	@Override
	public List<ResponsibilitiesResponse> canAssignGrievanceTo(ResponsibilitiesRequest responsibilitiesRequest) {
		Department department = departmentRepository.findById(responsibilitiesRequest.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("Department","Department ID",responsibilitiesRequest.getDepartmentId()));
		Category category = categoryRepository.findById(responsibilitiesRequest.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category","Category ID",responsibilitiesRequest.getCategoryId()));
		
		List<Responsibilities> responsibilities = responsibilitiesRepo.findByDepartmentAndCategory(department,category);
		List<ResponsibilitiesResponse> respUser = responsibilities.stream().map((R)-> this.modelMapper.map(R, ResponsibilitiesResponse.class)).collect(Collectors.toList());						
		return respUser;
	}

	
	@Override
	public GrievenceResponse assignGrievance(String grievanceRefNo,String empId) {
		Grievence assignedGrievence = null;
		Grievence grievence = grievenceRepo.findByRefNo(grievanceRefNo);
		if(grievence != null) {
			User user = userRepo.findByEmployeeId(empId.toUpperCase()).orElseThrow(()->new ResourceNotFoundException("Employee", " Employee Id ", empId ));
			if(user != null) {
				grievence.setCurrentUser(user);	
				grievence.setGrievanceStatus(AppConstants.FORWARDED_GRIEVANCE_STATUS);
				assignedGrievence = grievenceRepo.save(grievence);
				
				saveActionPerfomed(AppConstants.FORWARDED_GRIEVANCE_STATUS, user, grievence);
			}
		}else {
			throw new ResourceNotFoundException("Grievence", " Grievence Refrence Number ", grievanceRefNo);
		}
		return this.modelMapper.map(assignedGrievence, GrievenceResponse.class);
	}
	
	
	@Override
	public ResponseEntity<Object> callExternalAPiFunc(@Valid EmpMasterRequest empMasterRequest) throws Exception {

        ResponseEntity<Object> ret = null;
        
        MyRequestBody requestBody = new MyRequestBody();
        requestBody.setClient_id(AppConstants.CLIENTID);
        requestBody.setClient_secret(AppConstants.CLIENTSECRET);
        requestBody.setClient_serID(AppConstants.CLIENTSERID);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HttpEntity
        HttpEntity<MyRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange(
        	AppConstants.AUTHENTICATEURL,
            HttpMethod.POST,
            request,
            String.class
        );
        
        // Assuming response.getBody() returns a JSON string
        String authenticate = response.getBody();
        
        // Create a JSONParser instance
        JSONParser parser = new JSONParser();
        
        try {
        	//if authenticate is not null
        	if(authenticate != null) {
        		// Parse the JSON string into an Object
	            Object obj = parser.parse(authenticate);
	            
	            // Cast the Object to a JSONObject
	            JSONObject jsonObject = (JSONObject) obj;
	            
	            // Extract the access_token
	            String access_token = (String) jsonObject.get("access_token");
	            
	            //if access_token is not null
	            if(access_token != null) {
	            	EmpMasterRequest empResponse = new EmpMasterRequest();
	            	ResponseEntity<String> res = callApi(access_token,AppConstants.SEARCHMODE,empMasterRequest.getEmployeeId(),AppConstants.ORGCODE);
	            	
	            	if(res.getBody() != null && res.getStatusCodeValue() == 200) {
	            	
						int randomNumber = ApplicationUtility.generateOTP(6);
						String randomKey = ApplicationUtility.generatePassword(6);
						
	    				String empDetails = res.getBody();
	    				
	    	            JSONObject jsonObj = (JSONObject) parser.parse(empDetails);
	    	            		    	            
	    	            JSONArray dataArray = (JSONArray) jsonObj.get("Data");
	    	            
	    	         // Optional   
	    	         // Iterate through the array
//	    	            for (Object obj0 : dataArray) {
//	    	                JSONObject dataObj = (JSONObject) obj0;
//
//	    	                // Extract the "name" field from the JSON object
//	    	                String name = (String) dataObj.get("name");
//	    	            }
	    	            
	    	            // Get the first object in the array
	    	            if (dataArray != null & dataArray.size() > 0) {
	    	                JSONObject firstObject = (JSONObject) dataArray.get(0);

	    	                // Extract and print the "name" field from the first JSON object
	    	                String name = (String) firstObject.get("name");
	    	                
	    	                String mobNo = "";
	    	                try
	    	                {
	    	                	mobNo = ((Long) firstObject.get("mobile_number")).toString();
	    	                }
	    	                catch(Exception e)
	    	                {
	    	                	if(e.toString().contains("String cannot be cast to class java.lang.Long"))
	    	                	{
	    	                		mobNo = "";
	    	                	} 		
	    	                }
	    	                
	    	                String email = (String) firstObject.get("email_address");
	    	                
	    	                User user = this.getUserByEmpId(empMasterRequest.getEmployeeId());
							//if user found
	    	                if(user != null) {
								user.setOtp(randomNumber);
				        		user.setOtpSentOn(new Date());
				        		User savedOtp = userRepo.save(user);
				        		if(savedOtp != null) {
				        			empResponse = modelMapper.map(savedOtp, EmpMasterRequest.class);
				        			if(sendMessage) {
				        				textMessageServices.sendMessage(user.getContactNo(),TextMessageTemplate.sendOTP(savedOtp.getOtp(),"proceed to raise a complaint."));					
				        				empResponse.setOtp(0);
				        				empResponse.setPassword("0");
				        			}else {
				        				empResponse.setOtp(randomNumber);
				        				empResponse.setPassword("0");
				        			}
				        		}
//				        		empResponse = modelMapper.map(savedOtp, EmpMasterRequest.class);
				        	}
	    	                //if user not found
							else if(user == null) {
								EmpMasterRequest empMasReq = new EmpMasterRequest();
								empMasReq.setEmployeeId(empMasterRequest.getEmployeeId());
								
								if(name != null) {
									empMasReq.setFullName(name);										
								}
								
								if(employeePath) {
									if(!mobNo.equalsIgnoreCase(null) && !mobNo.equalsIgnoreCase("")) {
										User userMobileNo = userRepo.findByContactNo(mobNo).orElse(null);
										if(userMobileNo == null) {											
											empMasReq.setContactNo(mobNo);																					
										}else {
											throw new AlreadyExistsException("Employee", "this Contact Number", mobNo);
										}
									}else {
										User userMobileNo = userRepo.findByContactNo(empMasterRequest.getContactNo()).orElse(null);
										if(userMobileNo == null) {
											empMasReq.setContactNo(empMasterRequest.getContactNo());																					
										}else {
											throw new AlreadyExistsException("Employee with", "Contact Number", empMasterRequest.getContactNo());
										}
//										throw new IllegalArgumentException("Contact Number do not exist. Kindly update your contact number on HRMs Portal.");
									}
								}else {
									if(empMasterRequest.getContactNo() != null) {
										User userMobileNo = userRepo.findByContactNo(empMasterRequest.getContactNo()).orElse(null);
										if(userMobileNo == null) {
											empMasReq.setContactNo(empMasterRequest.getContactNo());																					
										}else {
											throw new AlreadyExistsException("Employee with", "Contact Number", empMasterRequest.getContactNo());
										}
									}else {
										throw new IllegalArgumentException("Contact is Blank.");
									}
								}
								
								if(email != null) {
									empMasReq.setEmail(email);										
								}
								
								empMasReq.setOtp(randomNumber);

								empResponse = this.createNewUser(empMasReq,randomNumber,randomKey);
								if(empResponse != null) {
				        			if(sendMessage) {
				        				textMessageServices.sendMessage(empResponse.getContactNo(),TextMessageTemplate.sendOTP(randomNumber,"proceed to raise a complaint. "
				        						+ "Also, Your account has been successfully created." +
				        						" Your UserId is:" + empResponse.getEmployeeId() +
				        						" and Your temporary password is: " + randomKey + 
				        						". Please log in and change your password. Thank you for joining us!"));
				        			}else {
				        				empResponse.setOtp(randomNumber);
				        				empResponse.setPassword(randomKey);
				        			}
				        		}
				        	}
	    	            } else {
	    	            	throw new IllegalArgumentException("Employee not exist with Employee Id : " + empMasterRequest.getEmployeeId());
	    	            }
	            		
	            		ret = new ResponseEntity<Object>(empResponse,HttpStatus.CREATED);
	            		
	            	}else {
	            		ret = new ResponseEntity<Object>("Not found",HttpStatus.NOT_FOUND);;
	            	}
	            }else {
	            	//if access_token is null
	            	ret =  null;
	            }
        	}else {
        		//if authenticate is null
        		ret =  null;
        	}
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
	}



	
	public ResponseEntity<String> callApi(String accessToken, String searchMode, String searchKey, String orgCode) 
	{
		ResponseEntity<String> ret = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.set("searchMode", searchMode);
		headers.set("searchKey", searchKey);
		headers.set("orgCode", orgCode);
		headers.set("Content-Type", "application/json");
		   
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(AppConstants.EMPLOYEEDATAURL, HttpMethod.POST, entity, String.class);
		if(response.getBody() != null && response.getStatusCodeValue() == 200) {
			ret = response;
		}else {
			ret = new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);;
		}
		return ret;
	}
	
	
	@Override
	public EmpMasterRequest createNewUser(EmpMasterRequest body,int randomNumber,String randomKey) {
		EmpMasterRequest empRes = null;
		User newuser = new User();
		
		if(body.getFullName() != null) {
			newuser.setFullName(body.getFullName());			
		}else {
			throw new IllegalArgumentException("Employee Name is Blank. Kindly update your Name on HRMs Portal.");
		}
		
		if(body.getEmployeeId() !=null) {
			newuser.setEmployeeId(body.getEmployeeId());			
		}else {
			throw new IllegalArgumentException("EmployeeId is Blank.");
		}
		
		if(body.getContactNo() != null) {
			newuser.setContactNo(body.getContactNo());			
		}
		else {
			throw new IllegalArgumentException("Contact Number do not exist. Kindly update your contact number on HRMs Portal.");
		}

		newuser.setEmail(body.getEmail());
		
		//status
		newuser.setStatus(AppConstants.NEW_USER_STATUS);
		//enabled user
		newuser.setEnabled(true);
		//encoded the password
		String passKey = this.passwordEncoder.encode(randomKey);
		newuser.setPassword(passKey);
		//registered on
		newuser.setRegisteredOn(new Date());
		//roles
		Role role = this.roleRepository.findByName(AppConstants.NORMAL_USER).get();	
		newuser.getRoles().add(role);
		newuser.setOtp(randomNumber);
		newuser.setOtpSentOn(new Date());
		User savedUser = userRepo.save(newuser);
		if(savedUser != null) {
			empRes = modelMapper.map(savedUser, EmpMasterRequest.class);
			if(sendMessage) {
				empRes.setOtp(0);	
				empRes.setPassword("0");
			}else {
				empRes.setOtp(savedUser.getOtp());
				empRes.setPassword(randomKey);				
			}
		}
		return empRes;
	}
	

//	@Override
//	public void sendMessage(String contactNo, String message) throws Exception {
//		System.out.println("Message sent to " + contactNo + ". " + message);
//		textMessageServices.sendMessage(contactNo, message);
//	}

	
//	@Override
//	public int generateRandomNumber() {
//		Random random = new Random();
//		int randomNumber = 100000 + random.nextInt(900000);
//		return randomNumber;
//	}
	
//--------------------------------------------------------------------------------------------------------------------------------		
// For AuthController 	
	
	@Override
	public UserResponse forgetUserPassword(EmployeeRequest user) throws Exception {
		User otpUpdated = null;
		if(user != null) {
			User existingUser = userRepo.findByEmployeeIdAndContactNo(user.getEmployeeId().toUpperCase(),user.getContactNo()).orElseThrow(()->new ResourceNotFoundException("User", " Employee Id ", user.getEmployeeId()));
			if(existingUser != null) {
				int randomNumber = ApplicationUtility.generateOTP(6);
		        
		        existingUser.setOtp(randomNumber);
		        existingUser.setOtpSentOn(new Date());
		        otpUpdated = userRepo.save(existingUser);
		        if(otpUpdated != null) {
					if(sendMessage) {
						textMessageServices.sendMessage(user.getContactNo(),TextMessageTemplate.sendOTP(randomNumber,"reset your password for your account. Please use the One-Time Password (OTP) below to proceed with resetting your password."));					
					}else {
						otpUpdated.setOtp(randomNumber);
						System.out.println(randomNumber);
					}
				}
		        return this.modelMapper.map(otpUpdated, UserResponse.class);
			}else {
				throw new ResourceNotFoundException("User ID" , "User ID", user.getEmployeeId());
			}
		}
		return this.modelMapper.map(otpUpdated, UserResponse.class);
	}
		
	
	@Override
	public UserResponse VerifyOtp(OtpRequest otp) {
		User existingUser = null;
		if(otp != null) {
			existingUser = userRepo.findByEmployeeIdAndContactNo(otp.getEmployeeId().toUpperCase(),otp.getContactNo()).orElseThrow(()->new ResourceNotFoundException("User", " Employee Id ", otp.getEmployeeId()));
			if(existingUser != null) {
				if(existingUser.getOtp() == otp.getOtp() && otp.getOtp() != 0) {
					return this.modelMapper.map(existingUser, UserResponse.class);					
				}						
			}
		}
		return this.modelMapper.map(existingUser, UserResponse.class);
	}
	
	
	@Override
	public UserResponse updatePassword(PassKeyRequest password) throws Exception {
		User existingUser = null;
		if(password != null) {
			existingUser = userRepo.findByEmployeeIdAndContactNo(password.getEmployeeId().toUpperCase(),password.getContactNo()).orElseThrow(()->new ResourceNotFoundException("User", " Employee Id ", password.getEmployeeId()));
			if(existingUser != null) {
				if(existingUser.getOtp() == password.getOtp() && password.getOtp() != 0) {
					existingUser.setPassword(this.passwordEncoder.encode(password.getPassword()));
					existingUser.setOtp(0);
					existingUser.setOtpSentOn(null);
					User updatedPassword = userRepo.save(existingUser);
					if(updatedPassword != null) {
						if(sendMessage) {
							textMessageServices.sendMessage(updatedPassword.getContactNo(),"Dear, Password for your User ID " + updatedPassword.getEmployeeId() +
     								"has been updated successfully. If not done by you, then change password immediately.");						
						}else {
							System.out.println(updatedPassword.getContactNo() + "Dear, Password for your User ID " + updatedPassword.getEmployeeId() +
     								"has been updated successfully. If not done by you, then change password immediately.");
						}
					}
					return this.modelMapper.map(updatedPassword, UserResponse.class);					
				}	
			}
		}
		return this.modelMapper.map(existingUser, UserResponse.class);
	}
	

//--------------------------------------------------------------------------------------------------------------------------------	
	

	
//--------------------------------------------------------------------------------------------------------------------------------	
//Change Password
	@Override
	public UserResponse changeUserPassword(ChangePassKey password,Principal principal) throws Exception {
		if(password != null && principal !=null) {
			String empId = principal.getName();
			User user = userRepo.findByEmployeeId(empId.toUpperCase()).orElse(null);
			if(user != null) {
				 boolean userpassword = passwordEncoder.matches(password.getOldPassword(),user.getPassword());
                 if (userpassword == true) {
                	 user.setPassword(passwordEncoder.encode(password.getNewPassword()));
                     User updatedUser = userRepo.save(user);
                     if(updatedUser != null) {
     					if(sendMessage) {
     						textMessageServices.sendMessage(user.getContactNo(),"Dear, Password for your User ID " + updatedUser.getEmployeeId() +
     								"has been changed successfully. If not done by you, then change password immediately.");					
     					}else {
     						System.out.println(user.getContactNo() + " Dear, Password for your User ID " + updatedUser.getEmployeeId() +
     								"has been changed successfully. If not done by you, then change password immediately.");
     					}
     				}
                     return this.modelMapper.map(updatedUser, UserResponse.class);
                 }
			}
		}
		return null;
	}


}
