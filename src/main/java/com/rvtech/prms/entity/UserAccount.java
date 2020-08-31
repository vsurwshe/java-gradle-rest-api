package com.rvtech.prms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the userAccount database table.
 * 
 */
@Entity
@Setter
@Getter
@Table(name = "useraccount")
public class UserAccount  extends BaseEntity<String> {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "accountId",length=100)
	private String accountId;

	@Column(nullable = false)
	private boolean active;


	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@Column(length = 15)
	private String lastloginIP;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogout;

	@Column(length = 512)
	private String fingerPrint;

	@Column(length = 512)
	private String passcode;

	@Column(nullable = false, length = 512)
	private String password;


	@Column(nullable = false, length = 512)
	private String userName;

	@Column(nullable = false, length = 512)
	private String emailid;

	@Column(length = 64)
	private int role;

	

	// Role id from roles table where customer menu is decided based on this.
	@Column
	private Long roleId;

	

}