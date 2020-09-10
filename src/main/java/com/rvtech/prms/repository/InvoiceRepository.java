package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.InvoiceEntity;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity,String>{

	
	@Query(value = "SELECT * FROM invoice WHERE projectId=:projectId  AND (fromDate BETWEEN :fDate AND :tDate OR toDate BETWEEN :fDate AND :tDate) order by createdOn desc", nativeQuery = true)
	List<InvoiceEntity> findinvoice(@Param(value = "projectId") String projectId,
			@Param(value = "fDate") String fDate, @Param(value = "tDate") String tDate);
}
