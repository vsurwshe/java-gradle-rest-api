package com.rvtech.prms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.ProjectEntity;

public interface ProjectRepository extends CrudRepository<ProjectEntity, String> {

	Optional<ProjectEntity> findById(String id);
	
	@Query(value="SELECT * FROM project WHERE id=:ProjectId", nativeQuery = true)
	ProjectEntity findByIdEntity(@Param("ProjectId")String ProjectId);

	List<ProjectEntity> findAllByProjectNameOrClientNameContainingOrClientIdContainingAndActive(String projectName,
			String clientName, String clientId, Boolean active, Pageable page);

	List<ProjectEntity> findAllByActive(Boolean active, Pageable page);
	
	List<ProjectEntity> findAllByActive(Boolean active);
	
	List<ProjectEntity> findByClientId(String clientId);
	

	@Modifying(clearAutomatically = true)
	@Query("UPDATE ProjectEntity c SET c.active = :active WHERE c.id = :projId")
	int updateActive(@Param("projId") String expId, @Param("active") Boolean active);
}
