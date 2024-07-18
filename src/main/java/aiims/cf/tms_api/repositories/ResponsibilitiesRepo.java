package aiims.cf.tms_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aiims.cf.tms_api.models.Category;
import aiims.cf.tms_api.models.Department;
import aiims.cf.tms_api.models.Responsibilities;
import aiims.cf.tms_api.models.User;

@Repository
public interface ResponsibilitiesRepo extends JpaRepository<Responsibilities, Long> {

//	Responsibilities findByDepartmentAndCategory(Department department, Category category);

	List<Responsibilities> findByUser(User user);

//	Responsibilities findByDepartment(Department department);
	
	List<Responsibilities> findByDepartment(Department department);
	

//	Responsibilities findByDepartmentAndUser(Department department, User user);
	
	List<Responsibilities> findByDepartmentAndUser(Department department, User user);

	List<Responsibilities> findByDepartmentAndCategoryAndUser(Department department, Category category, User user);

	List<Responsibilities> findByDepartmentAndCategory(Department department, Category category);

	List<Responsibilities> findByDepartmentAndUserAndStatus(Department department, User user, String status);

	List<Responsibilities> findByDepartmentAndCategoryAndUserAndStatus(Department department, Category category,
			User user, String status);


}
