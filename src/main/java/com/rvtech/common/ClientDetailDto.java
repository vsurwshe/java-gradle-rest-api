package com.rvtech.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientDetailDto {

	private String id;
	
	private String clientName;

	private String tanNum;

	private boolean active;

	private String tanUrl;

	private String gstNum;

	private String gstUrl;
	
	@Autowired
	private List<BankDetailsDto> bankDetailsDtoList;
	
	@Autowired
	private List<AddressDto> addressDtos;
	
	@Autowired
	private List<ContactPersonDto> contactPersonDtos;
	
	@Autowired
	private List<RateCardDto> rateCardDtos;

}
