package com.rvtech.prms.common;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AddressDto {


	private String id;
	
	private String addressLine;

	private boolean active;
	
	private String city;
	
	private String area;
	
	private String state;
	
	private String pincode;
	
	private String clientId;
}
