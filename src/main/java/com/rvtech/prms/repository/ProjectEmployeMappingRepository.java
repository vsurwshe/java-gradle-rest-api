package com.rvtech.prms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.ProjectEmployeMappingEntity;
import com.rvtech.prms.entity.ProjectEntity;

public interface ProjectEmployeMappingRepository extends CrudRepository<ProjectEmployeMappingEntity, String> {

	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE ProjectEmployeMappingEntity c SET c.active=:active WHERE c.id=:id")
	int updateActive(@Param("id") String id, @Param("active") Boolean active);
	
	Optional<ProjectEmployeMappingEntity> findById(String id);
	
	List<ProjectEmployeMappingEntity> findAllByActiveAndProjectIdOrRateCardId(Boolean active,String pid,String id, Pageable page);

	List<ProjectEmployeMappingEntity> findAllByActive(Boolean active, Pageable page);
	
	List<ProjectEmployeMappingEntity> findAllByProjectId(String projectId);
}
