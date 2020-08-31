package com.rvtech.prms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "addressdetails")
public class AddressDetailsEntity extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	@Column
	private String addressLine;

	@Column(name = "active", nullable = false, columnDefinition = "tinyint(1) default 1")
	private boolean active;

	@Column
	private String city;
	@Column
	private String area;
	@Column
	private String state;
	@Column
	private String pincode;
	
	@Column
	private String clientId;
}
