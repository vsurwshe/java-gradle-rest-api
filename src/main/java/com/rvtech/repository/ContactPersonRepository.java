package com.rvtech.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rvtech.entity.ContactPersonEntity;

public interface ContactPersonRepository extends CrudRepository<ContactPersonEntity, String> {
	
	List<ContactPersonEntity> findByClientId(String clientId);

}
