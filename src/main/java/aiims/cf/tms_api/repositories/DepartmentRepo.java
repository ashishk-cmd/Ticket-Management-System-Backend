package aiims.cf.tms_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aiims.cf.tms_api.models.Department;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
    
	public Optional<Department> findByName(String name);

	public List<Department> findAllByStatus(String status);
}
