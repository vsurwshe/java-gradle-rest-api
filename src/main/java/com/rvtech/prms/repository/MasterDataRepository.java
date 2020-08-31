package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.rvtech.prms.entity.MasterDataEntity;

public interface MasterDataRepository extends CrudRepository<MasterDataEntity, Long> {

	List<MasterDataEntity> findAllByActiveAndTypeOrCategoryContaining(boolean flag, String searchType,String searchCategory ,Pageable page);

	List<MasterDataEntity> findByActiveAndTypeOrCategoryContainingAndNameContaining(boolean flag, String searchType,String searchCategory, String searchTerm,
			Pageable page);
	
	MasterDataEntity  findByActiveAndId(boolean flag,Long id );

}
