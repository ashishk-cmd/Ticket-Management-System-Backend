package aiims.cf.tms_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;



@Entity
public class Responsibilities {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;

	private String status;
	
	@ManyToOne
	private User user;

	@ManyToOne
	private Department department;
	
	@ManyToOne
	private Category category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Responsibilities(Long id, String status, User user, Department department, Category category) {
		super();
		this.id = id;
		this.status = status;
		this.user = user;
		this.department = department;
		this.category = category;
	}

	public Responsibilities() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Responsibilities [id=" + id + ", status=" + status + ", user=" + user + ", department=" + department
				+ ", category=" + category + "]";
	}

	
}
