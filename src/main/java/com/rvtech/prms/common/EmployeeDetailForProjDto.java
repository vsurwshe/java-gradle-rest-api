package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDetailForProjDto implements Serializable {

	/**
	 * This object used for employee assignemnt in project
	 */
	private static final long serialVersionUID = 1L;

	private String accountId;

	private String firstName;

	private String lastName;

	private String mobileNumber;

	private String emailId;

	private String designation;

	private String employeeNumber;

	private String rateCardId;

	private String category;

	private Float toExperience;
	
	private Float fromExperience;

	private String skill;

	private String domain;

	private Date onbordaingDate;
	
	private Date exitDate;
	
	private String emploeeMappedId;
}
