package com.rvtech.prms.entity;

import java.util.Date;

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
@Table(name = "project")
public class ProjectEntity extends BaseEntity<String> {

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
	private String projectName;

	@Column(name = "active", nullable = false, columnDefinition = "tinyint(1) default 1")
	private boolean active;

	@Column
	private String projectDesc;

	@Column
	private Float projectCost;

	@Column
	private String projectType;

	@Column
	private String contractAttachmentUrl;

	@Column
	private String projectManager;

	@Column
	private String purchaseOrder;
	
	@Column
	private String purchaseOrderId;

	@Column
	private Date projectStartDate;

	@Column
	private Date projectEndDate;

	@Column
	private String clientName;
	
	@Column
	private String clientId;


}
