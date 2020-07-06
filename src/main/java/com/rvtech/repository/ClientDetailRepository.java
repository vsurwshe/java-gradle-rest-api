package com.rvtech.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.rvtech.entity.ClientDetailsEntity;
import com.rvtech.entity.PurchaseOrderEntity;

public interface ClientDetailRepository extends CrudRepository<ClientDetailsEntity, String> {

	ClientDetailsEntity findByIdAndActive(String id, Boolean active);

	List<ClientDetailsEntity> findByClientNameContaining(String clientName, Pageable page);

	List<ClientDetailsEntity> findAllByActiveTrueOrderByCreatedOnDesc(Pageable page);

}
