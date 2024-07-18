package aiims.cf.tms_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aiims.cf.tms_api.models.Grievence;
import aiims.cf.tms_api.models.Remarks;

@Repository
public interface RemarksRepo extends JpaRepository<Remarks, Long> {

	List<Remarks> findByGrievence(Grievence grievence);


}
