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
@Table(name = "purchaseorder")
public class PurchaseOrderEntity extends BaseEntity<String> {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	@Column
	private String poNum;

	@Column(name = "active", nullable = false, columnDefinition = "tinyint(1) default 1")
	private boolean active;

	@Column
	private Float poAmount;

	@Column
	private Date validFrom;

	@Column
	private Date validTo;

	@Column
	private String clientName;
	
	@Column
	private Float balPoAmt;
	
	@Column
	private String poCntrUrl;
	
	@Column
	private Float invAmnt;
	
	@Column
	private String clientId;


}
