package com.rvtech.prms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.PurchaseOrderEntity;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrderEntity, String> {

	Optional<PurchaseOrderEntity> findByIdAndActive(String id, Boolean active);
	
	List<PurchaseOrderEntity> findByActive(Boolean active, Pageable page);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE PurchaseOrderEntity c SET c.active = :active WHERE c.id = :poId")
	int updateActive(@Param("poId") String poId, @Param("active") Boolean active);

	List<PurchaseOrderEntity> findByClientNameContainingOrClientIdContaining(String clientName,String clientId, Pageable page);
	
	
}
