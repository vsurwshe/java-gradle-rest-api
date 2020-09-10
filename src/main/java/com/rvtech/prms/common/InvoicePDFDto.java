package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvoicePDFDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String invoiceNo;

	private String invoiceDate;

	private String toCompanyName;

	private String fromCompanyName = "RIGVED Technologies Private Ltd";

	private String toCompanyAddress;

	private String fromcompanyAddress = "04th Floor, 408, Plot no A-1, Rupa Solitaire,"
			+ "Millennium Business Park, Mahape," + "Mumbai, Maharashtra, 400710, India";

	private String toCompanyState;

	private String fromCompanyState = "Maharashtra";

	private String toGstNo;

	private String fromGstNo = "27AAECR1228G1ZL";

	private String toStateCode;

	private String fromStateCode = "27";

	private String toPanNo;

	private String fromPANNo = "AAECR1228G";

	private String fromSACCode = "996601";

	private Double sGSTUTGS;

	private Double cGST;

	private Double iGST;

	private Double billWitoutGST;

	private Double billWitGST;

	private String totalBillingInWords;

	private String contactNo = "9004353333";

	private String emailId = "deepika.singh@rigvedtech.com";

	private String cin = "U74900MH2008PTC186830";

}
