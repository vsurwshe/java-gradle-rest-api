package com.rvtech.common;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Component
public class UserAccountDto {

	/** The serialVersionUID of type long. */
	private static final long serialVersionUID = -686595278462641122L;

	private String accountId;

	private String userName;

	private Date lastLogin;

	private String lastloginIP;

	private Date lastLogout;

	private int role;

	private String emailId;

	
}