package com.rvtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.entity.PurchaseOrderEntity;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrderEntity, String> {

	Optional<PurchaseOrderEntity> findByIdAndActive(String id, Boolean active);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE PurchaseOrderEntity c SET c.active = :active WHERE c.id = :poId")
	int updateActive(@Param("poId") String poId, @Param("active") Boolean active);

	List<PurchaseOrderEntity> findByClientNameContaining(String clientName, Pageable page);
	
	
}
