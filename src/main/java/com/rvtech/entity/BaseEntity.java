package com.rvtech.entity;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	

    @CreatedDate
    @Column(nullable = false, updatable = false)
	protected ZonedDateTime createdOn=ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
    
    @LastModifiedDate
    @Column(nullable = false)
	protected ZonedDateTime updatedOn=ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

    @PrePersist
	protected void onCreate() {
		//ZoneId.of("Asia/Kolkata");
    	updatedOn = createdOn = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
	}

	@PreUpdate
	protected void onUpdate() {
		//ZoneId.of("Asia/Kolkata")
		updatedOn = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
	}
	
	
    @Column(nullable = false, updatable =false)
	protected String createdBy ="admin";

    @Column(nullable = false)
	protected String updatedBy ="admin";

	/*
	 * @Column(nullable = false) protected String status=Status.ACTIVE.toString();
	 */
}