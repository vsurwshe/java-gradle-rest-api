package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto implements Serializable{

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
}
