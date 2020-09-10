package com.rvtech.prms.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rvtech.prms.entity.ClientDetailsEntity;

public interface ClientDetailRepository extends CrudRepository<ClientDetailsEntity, String> {

	ClientDetailsEntity findByIdAndActive(String id, Boolean active);

	List<ClientDetailsEntity> findByClientNameContaining(String clientName, Pageable page);

	List<ClientDetailsEntity> findAllByActiveTrueOrderByCreatedOnDesc(Pageable page);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE ClientDetailsEntity c SET c.active = :active WHERE c.id = :clientId")
	int updateActive(@Param("clientId") String clientId, @Param("active") Boolean active);

	@Query("select c.clientName,c.id from ClientDetailsEntity c WHERE c.active =true")
	List<Object[]> allclient();

	@Query(value = "SELECT pr.clientId, COUNT(prmap.id) FROM   project pr,"
			+ "    projectemployemapping prmap WHERE prmap.projectId = pr.id"
			+ "        AND (prmap.exitDate IS NULL || (DATE(prmap.exitDate) >= :date)) "
			+ "        and DATE(prmap.onbordaingDate) <= :date GROUP BY pr.clientId", nativeQuery = true)
	List<Object[]> employeeCountByClient(@Param("date") String date);

	@Query(value = "SELECT pr.clientId, COUNT(prmap.id) FROM   project pr,"
			+ "    projectemployemapping prmap WHERE pr.clientId=:clientId and prmap.projectId = pr.id"
			+ "        AND (prmap.exitDate IS NULL || (DATE(prmap.exitDate) >= :date)) "
			+ "        and DATE(prmap.onbordaingDate) <= :date GROUP BY pr.clientId", nativeQuery = true)
	List<Object[]> employeeCountByClient(@Param("clientId") String clientId, @Param("date") String date);

	@Query(value = "SELECT clientId,sum(billWitoutGST) FROM invoicepdf where  month(createdOn)=:month and year(createdOn)=:year  group by clientId", nativeQuery = true)
	List<Object[]> invoiceAmountSumByClientId(@Param("month") int month, @Param("year") int year);

	@Query(value = "SELECT clientId,sum(billWitoutGST) FROM invoicepdf where clientId=:clientId and  month(createdOn)=:month and year(createdOn)=:year  group by clientId", nativeQuery = true)
	List<Object[]> invoiceAmountSumByClientId(@Param("month") int month, @Param("year") int year,
			@Param("clientId") String clientId);

	@Query(value = "SELECT count(*) FROM projectemployemapping WHERE DATE(onbordaingDate) BETWEEN :startDate AND :endDate", nativeQuery = true)
	int noOfEmployeJoined(@Param("startDate") String startDate, @Param("endDate") String endDate);

	@Query(value = "SELECT count(*) FROM projectemployemapping WHERE DATE(exitDate) BETWEEN :startDate AND :endDate", nativeQuery = true)
	int noOfEmployeExit(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
