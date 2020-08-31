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
@Table(name = "contactperson")
public class ContactPersonEntity extends BaseEntity<String>{
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	@Column
	private String name;

	@Column(name = "active", nullable = false, columnDefinition = "tinyint(1) default 1")
	private boolean active;

	@Column
	private String email;
	
	@Column
	private String mobileNum;
	
	@Column
	private String clientId;
	
	@Column
	private String role;

}
