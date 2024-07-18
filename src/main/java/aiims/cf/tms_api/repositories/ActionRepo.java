package aiims.cf.tms_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aiims.cf.tms_api.models.Action;

@Repository
public interface ActionRepo extends JpaRepository<Action, Long> {

	
}
