package com.rvtech.prms.common;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttendanceDto {

	private String id;
	
	private String month;
	
	private String employeeId;
	
	private String employeeName;
	
	private String branchNmae;
	
	private String designation;
	
	private String companyName;
	
	private String dayPresent;
	
	private String weekOff;
	
	private String halfDay;
	
	private String absent;
	
	private Date fromDate;
	
	private Date toDate;
	
	private String accountId;

	
}
