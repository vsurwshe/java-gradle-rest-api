package com.rvtech.prms.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.rvtech.prms.common.CommanDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "invoicedetail")
public class InvoiceDetailEntity extends BaseEntity<String> {

	/**
	 * 
	 */
	@Column
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	@Column
	private String employeeName;

	@Column
	private String employeeId;

	@Column
	private Float totalDays;

	@Column
	private Float perDayRate;

	@Column
	private String attendancepermonth;

	

	@Column
	private Float totalAmt;

	@Column
	private Boolean active;
	
	@Column
	private String invoiceId;

}
