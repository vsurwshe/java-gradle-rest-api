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

	@Query(value = "SELECT  pr.clientId, COUNT(prmap.id) FROM project pr,projectemployemapping prmap WHERE prmap.projectId = pr.id AND (prmap.exitDate is null or prmap.exitDate between :startDate and :endDate) GROUP BY pr.clientId", nativeQuery = true)
	List<Object[]> employeeCountByClient(@Param("startDate") String startDate, @Param("endDate") String endDate);

	@Query(value = "SELECT  pr.clientId, COUNT(prmap.id) FROM project pr,projectemployemapping prmap WHERE prmap.projectId = pr.id and pr.clientId=:clientId AND (prmap.exitDate is null or prmap.exitDate between :startDate and :endDate) GROUP BY pr.clientId", nativeQuery = true)
	List<Object[]> employeeCountByClientId(@Param("clientId") String clientId, @Param("startDate") String startDate,
			@Param("endDate") String endDate);

}
