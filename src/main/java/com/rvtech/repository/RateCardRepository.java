package com.rvtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rvtech.entity.ContactPersonEntity;
import com.rvtech.entity.ProjectEntity;
import com.rvtech.entity.RateCardEntity;

public interface RateCardRepository extends CrudRepository<RateCardEntity, String> {
	
	Optional<RateCardEntity> findById(String id);

	List<RateCardEntity> findByClientId(String clientId);


}
