package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.ExpenseEntity;

public interface ExpenceRepository extends CrudRepository<ExpenseEntity, String> {

	List<ExpenseEntity> findAllByProjectIdAndActive(String projectId, Boolean active, Pageable page);

	List<ExpenseEntity> findAllByActive(Boolean active, Pageable page);
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE ExpenseEntity c SET c.active = :active WHERE c.id = :expId")
	int updateActive(@Param("expId") String expId, @Param("active") Boolean active);
	
	@Modifying(clearAutomatically = true)
	@Query("select amount from ExpenseEntity WHERE id = :expId")
	int findExpById(@Param("expId") String expId);
}
