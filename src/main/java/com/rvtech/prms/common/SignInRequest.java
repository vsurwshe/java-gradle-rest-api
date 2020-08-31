package com.rvtech.prms.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "Using this, Partner user can log in and can register the application in oxygen global portal or can see list of applications registered in the portal and etc;")
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4920258251896527420L;

	@NotNull(message="User Name is mandatory")
	@Size(min = 4, max = 50,message="User Name should have atleast 4 and Maximum of 50 characters")
	//@Pattern(regexp = "[a-zA-Z]{4,10}$",message = "userName is invalid")
	@ApiModelProperty(notes = "Parner User's Sign-In Id", required = true, allowEmptyValue = false, example = "Siva")
	private String userName;

	@NotNull(message="password is mandatory")
	@Size(min = 6, max = 15 ,message="Password should have atleast 6 and Maximum of 15 characters")
	@Pattern(regexp = "^(?=[^\\d_].*?\\d)\\w(\\w|[!@#$%^&*_=+-/]){7,15}",message = "password is invalid")
	@ApiModelProperty(notes = "Password of the partner user ", required = true, allowEmptyValue = false, example = "Abc@12def")
	private String password;
	
	@NotNull(message="deviceToken is mandatory")
	private String deviceToken;

	private String loginType="DEFAULT";
}
