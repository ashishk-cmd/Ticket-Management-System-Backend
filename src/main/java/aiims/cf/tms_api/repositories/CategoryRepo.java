package aiims.cf.tms_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aiims.cf.tms_api.models.Category;
import aiims.cf.tms_api.models.Department;
import aiims.cf.tms_api.models.User;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

	public Optional<Category> findByName(String name);

	public List<Category> findAllByStatus(String status);

	

}
