package com.rvtech.prms.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDisplayInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String accountId;
	
	private String firstName;
	
	private String lastName;
	
	private String mobileNumber;
	
	private String emailId;
	
	private String designation;
	
	private String employeeNumber;
	
	private String domain;

}
