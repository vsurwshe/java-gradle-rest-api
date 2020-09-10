package com.rvtech.prms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.RateCardEntity;

public interface RateCardRepository extends CrudRepository<RateCardEntity, String> {
	
	Optional<RateCardEntity> findById(String id);

	List<RateCardEntity> findByClientId(String clientId);

	@Query(value = "select rate,rateCardDuration,rateCardType from ratecard  where id=:ratecardId",nativeQuery = true)
	List<String> findRateById(@Param(value = "ratecardId") String ratecardId);
}
