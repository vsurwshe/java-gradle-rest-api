package com.rvtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.entity.UserAccount;

/**
 * Repository interface for {@link UserAccount} instances. Provides basic CRUD
 * operations due to the extension of {@link JpaRepository}. Specifies methods
 * used to obtain and modify User Allergy related information which is stored in
 * the database. This Repository supports Querydsl Predicates by extending
 * {@link QueryDslPredicateExecutor} interface.
 * 
 * @author Amit Patgaonkar
 */
public interface AccountRepository extends CrudRepository<UserAccount, String> {

	UserAccount findByuserName(String userName);

	// @Query(value = "select * from programdetails pd where
	// FIND_IN_SET(:binId,pd.binId)", nativeQuery = true)
	@Query(value = "select * from useraccount  where (userName=:userName or emailid=:userName) and active=true", nativeQuery = true)
	UserAccount findByMobileAndEmailId(@Param(value = "userName") String userName);
	
	UserAccount findOneByAccountIdAndUserName(String accountId,String userName);
}
