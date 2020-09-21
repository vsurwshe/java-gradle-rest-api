package com.rvtech.prms.common;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class InvoiceListDTO {

	private String clientName;

	private Date invoiceDate;

	private String invoiceNumber;

	private String personName;

	private Date dueDate;

	private Float totalAmount;
	
	private String invoicePdfId;
	
	public InvoiceListDTO() {
		super();
	}

	public InvoiceListDTO(String clientName, Date invoiceDate, String invoiceNumber, String personName, Date dueDate,
			Float totalAmount, String invoicePdfId) {
		super();
		this.clientName = clientName;
		this.invoiceDate = invoiceDate;
		this.invoiceNumber = invoiceNumber;
		this.personName = personName;
		this.dueDate = dueDate;
		this.totalAmount = totalAmount;
		this.invoicePdfId = invoicePdfId;
	}

}
