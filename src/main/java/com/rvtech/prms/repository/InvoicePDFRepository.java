package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.InvoicePDFEntity;

public interface InvoicePDFRepository extends CrudRepository<InvoicePDFEntity,String>{

	
	@Query(value = "SELECT * FROM invoicepdf WHERE poid=:poid  AND (fromDate BETWEEN :fDate AND :tDate OR toDate BETWEEN :fDate AND :tDate) order by createdOn desc", nativeQuery = true)
	List<InvoicePDFEntity> findinvoice(@Param(value = "poid") String poid,
			@Param(value = "fDate") String fDate, @Param(value = "tDate") String tDate);
}
