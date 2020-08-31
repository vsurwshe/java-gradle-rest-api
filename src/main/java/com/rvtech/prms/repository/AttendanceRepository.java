package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.AttendanceEntity;

public interface AttendanceRepository extends CrudRepository<AttendanceEntity, String> {

	@Query(value = "SELECT * FROM attendance WHERE accountId=:accountId  AND (fromDate BETWEEN :fDate AND :tDate OR toDate BETWEEN :fDate AND :tDate)", nativeQuery = true)
	List<AttendanceEntity> findattendance(@Param(value = "accountId") String accountId,
			@Param(value = "fDate") String fDate, @Param(value = "tDate") String tDate);

}
