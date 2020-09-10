package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InnvoiceDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String id;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Float totalInvAmt;
	
	private List<String> expList;
	
	private String projectId;
	
	
}
