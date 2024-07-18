package aiims.cf.tms_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aiims.cf.tms_api.models.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

	 public Optional<Role> findByName(String name);

	public Optional<Role> findById(Long roleId);
	
	
}
