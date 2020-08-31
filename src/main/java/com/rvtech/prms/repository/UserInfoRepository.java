package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.UserInfoEntity;

public interface UserInfoRepository extends CrudRepository<UserInfoEntity, String> {

	List<UserInfoEntity> findByFirstNameContainingOrLastNameContainingAndUserTypeContaining(String firstName,
			String lastName, String userType, Pageable page);
	
	UserInfoEntity findByaccountIdOrEmployeeNumber(String accountId,String employeeID);
	
	@Query(value = "select accountId from UserInfoEntity  where employeeNumber=:employeeNumber")
	String findAccountIdByEmpId(@Param(value = "employeeNumber") String employeeNumber);
}
