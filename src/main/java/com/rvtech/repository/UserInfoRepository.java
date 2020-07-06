package com.rvtech.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.rvtech.entity.UserInfoEntity;

public interface UserInfoRepository extends CrudRepository<UserInfoEntity, String> {

	List<UserInfoEntity> findByFirstNameContainingOrLastNameContainingAndUserTypeContaining(String firstName,
			String lastName, String userType, Pageable page);
}
