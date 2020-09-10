package com.rvtech.prms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.rvtech.prms.common.CommanDto;
import com.rvtech.prms.common.MasterDataDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "expense")
public class ExpenseEntity extends BaseEntity<String> {

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
	private String projectId;

	
	@Column
	private Long expTypeId;

	@Column
	private Float amount;

	@Column(name = "active", nullable = false, columnDefinition = "tinyint(1) default 1")
	private boolean active;

	
	@Column
	private String attachmentUrl;

	@Column
	private Date expDate;

	@Column
	private String description;

}
