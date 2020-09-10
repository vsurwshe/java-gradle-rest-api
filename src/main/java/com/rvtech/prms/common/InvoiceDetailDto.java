package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvoiceDetailDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String employeeName;

	private String employeeId;

	private String totalDays;

	private Float perDayRate;

	private String attendancepermonth;

	private List<CommanDto> monthAttendanceLiat;

	private Float totalInvPerEmp;

	private Float totalAmt;

	private Boolean active;
	

}
