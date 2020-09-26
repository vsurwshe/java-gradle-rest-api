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
@Table(name = "attendance")
public class AttendanceEntity {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	
	@Column
	private String employeeId;

	@Column
	private String employeeName;

	@Column
	private String branchNmae;

	@Column
	private String designation;

	@Column
	private String companyName;

	@Column(columnDefinition = "TEXT")
	private String dayPresent;

	@Column(columnDefinition = "TEXT")
	private String weekOff;

	@Column(columnDefinition = "TEXT")
	private String halfDay;

	@Column(columnDefinition = "TEXT")
	private String absent;

	@Column
	private Date fromDate;

	@Column
	private Date toDate;
	
	@Column
	private String accountId;


}
