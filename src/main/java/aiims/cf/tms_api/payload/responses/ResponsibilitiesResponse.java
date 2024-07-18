package aiims.cf.tms_api.payload.responses;

import java.util.List;

import aiims.cf.tms_api.models.Category;
import aiims.cf.tms_api.models.Department;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ResponsibilitiesResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	private UserResponse user;
	private Department department;
	private List<Category> assignedCategory;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UserResponse getUser() {
		return user;
	}
	public void setUser(UserResponse user) {
		this.user = user;
	}

	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public List<Category> getAssignedCategory() {
		return assignedCategory;
	}
	public void setAssignedCategory(List<Category> assignedCategory) {
		this.assignedCategory = assignedCategory;
	}
	
	
	
	
	
}
