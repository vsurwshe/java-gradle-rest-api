package com.rvtech.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rvtech.entity.BankDetailsEntity;

public interface BankDetailsRepository extends CrudRepository<BankDetailsEntity, String> {

	List<BankDetailsEntity> findByClientId(String clientId);
	
}
