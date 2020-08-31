package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseOrderDto implements Serializable {

	/** The serialVersionUID of type long. */
	private static final long serialVersionUID = -686595278462641122L;

	private String id;

	private String poNum;

	private boolean active;

	private Float poAmount;

	private Date validFrom;

	private Date validTo;

	private String clientName;
	
	private String clientId;

	private Float balPoAmt;

	private String poCntrUrl;
	
	private Float invAmnt;
}
