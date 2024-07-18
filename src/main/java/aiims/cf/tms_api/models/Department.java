package aiims.cf.tms_api.models;

import java.util.ArrayList;
import java.util.List;

import aiims.cf.tms_api.payloads.DepartmentDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

@Entity
public class Department {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	private String name;
	private String status;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
	@JoinTable(name="department_categories",
	joinColumns = @JoinColumn(name="department_id",referencedColumnName = "id",unique=false),
	inverseJoinColumns = @JoinColumn(name="categories_id",referencedColumnName = "id",unique=false)
		 )
	private List<Category> categories = new ArrayList<>();

	public static Department getInstance(DepartmentDto department) {
		Department data = new Department();
		data.setName(department.getName());
		data.setCategories(new ArrayList<>());
		return data;
	}

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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", status=" + status + ", categories=" + categories + "]";
	}
	
	
}
