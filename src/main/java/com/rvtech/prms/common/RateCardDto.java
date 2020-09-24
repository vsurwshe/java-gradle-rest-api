package com.rvtech.prms.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateCardDto {

	private String id;

	private boolean active;

	private String domainName;

	private Float fromYearOfExp;

	private Float toYearOfExp;

	private String skillCategory;

	private String skillSet;

	private String clientId;
	
	private Float rate;
	
	//To specify Monthly,Hourly,Daily
	private String rateCardType="Daily";
	
	//To Specify duration of rateCardType 
	private Float rateCardDuration=(float) 0;
}
