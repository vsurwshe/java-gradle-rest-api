package com.rvtech.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rvtech.entity.AddressDetailsEntity;
import com.rvtech.entity.BankDetailsEntity;

public interface AddressRepository extends CrudRepository<AddressDetailsEntity, String> {

	
	List<AddressDetailsEntity> findByClientId(String clientId);

}
