package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rvtech.prms.entity.AddressDetailsEntity;
import com.rvtech.prms.entity.BankDetailsEntity;

public interface AddressRepository extends CrudRepository<AddressDetailsEntity, String> {

	
	List<AddressDetailsEntity> findByClientId(String clientId);

}
