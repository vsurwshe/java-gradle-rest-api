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
@Table(name = "invoicepdf")
public class InvoicePDFEntity extends BaseEntity<String> {

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
	private String clientId;

	@Column
	private Date fromDate;

	@Column
	private Date toDate;

	@Column
	private String invoiceNo;

	@Column
	private String invoiceDate;

	@Column
	private String toCompanyName;

	@Column
	private String fromCompanyName;

	@Column
	private String toCompanyAddress;

	@Column
	private String fromcompanyAddress;

	@Column
	private String toCompanyState;

	@Column
	private String fromCompanyState;

	@Column
	private String toGstNo;

	@Column
	private String fromGstNo;

	@Column
	private String toStateCode;

	@Column
	private String fromStateCode;

	@Column
	private String toPanNo;

	@Column
	private String fromPANNo;

	@Column
	private String fromSACCode;

	@Column
	private Double sGSTUTGS;

	@Column
	private Double cGST;

	@Column
	private Double iGST;

	@Column
	private Float billWitoutGST;

	@Column
	private Float billWitGST;

	@Column
	private String totalBillingInWords;
	
	@Column
	private String poid;
}
