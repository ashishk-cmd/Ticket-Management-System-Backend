package aiims.cf.tms_api.models;

import aiims.cf.tms_api.payloads.CategoryDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	private String name;
	private String status;
	
	public static Category getInstance(CategoryDto category) {
		Category data = new Category();
		data.setName(category.getName());
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

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", status=" + status + "]";
	}


}
