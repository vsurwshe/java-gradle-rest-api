package com.rvtech.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankDetailsDto {

	private String id;

	private String accountNumber;

	private boolean active;

	private String canceledChaqueUrl;

	private String ifscCode;

	private String clientId;
}
