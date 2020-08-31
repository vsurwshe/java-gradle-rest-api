package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class RegistrationDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String emailId;
	private String gender;
	private Date dOB;
	private String designation;
	private String domain;
	private String companyName;
	private String employeeNumber;
	private Float cTC;
	private String primerySkill;// (Multiple comma suparated )
	private String secounderySkill;// - String (Multiple comma suparated )
	private String password;
	private String userType;
	private Float expInYears;
	private String profilePic;
	private Date joiningDate;
}
