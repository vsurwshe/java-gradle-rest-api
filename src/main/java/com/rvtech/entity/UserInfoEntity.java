package com.rvtech.entity;

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
@Table(name = "userinfo")
public class UserInfoEntity extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "memberId", length = 100)
	private String memberId;

	@Column
	private String accountId;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private String mobileNumber;

	@Column
	private String emailId;

	@Column
	private String gender;

	@Column
	private Date dOB;

	@Column
	private String designation;

	@Column
	private String domain;

	@Column
	private String companyName;

	@Column
	private String employeeNumber;

	@Column
	private Float cTC;

	@Column
	private String primerySkill;// (Multiple comma suparated )

	@Column
	private String secounderySkill;// - String (Multiple comma suparated )
	
	@Column
	private String userType;// - String ("administrator" )

	@Column
	private String profilePic;
	
	@Column
	private Float expInYears;
}
