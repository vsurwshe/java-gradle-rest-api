package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExpenseDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String id;

	private String attachmentUrl;

	private CommanDto project;
	
	private MasterDataDto expType;
	
	private Float amount;
	
	private Boolean active;
	
	private Date expDate;
	
	private String description;

}
