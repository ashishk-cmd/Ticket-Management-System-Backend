package aiims.cf.tms_api.payloads;

import java.util.ArrayList;
import java.util.List;


public class DepartmentDto {

	private Long id;
	private String name;
	private String status;

	private List<CategoryDto> categories = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CategoryDto> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDto> categories) {
		this.categories = categories;
	}
	
	
}
