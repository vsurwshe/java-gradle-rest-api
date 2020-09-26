package com.rvtech.prms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.common.ClientDetailDto;
import com.rvtech.prms.common.InnvoiceDto;
import com.rvtech.prms.common.InvoiceGenDto;
import com.rvtech.prms.services.InnviceServiceImpl;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/innvoice")
public class InnviceController {

	@Autowired
	private InnviceServiceImpl innviceServiceImpl;
	
	@PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody InnvoiceDto innvoiceDto) {
		return innviceServiceImpl.create(innvoiceDto);
	}
	
	
	@PostMapping(path = "/createPDF", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createPDF(@RequestBody InvoiceGenDto innvoiceDto) {
		return innviceServiceImpl.generatePDF(innvoiceDto);
	}
	
	@GetMapping(path = "/invoiceList/{pageIndex}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getInvoiceList(@PathVariable("pageSize") int pageSize,
			@PathVariable("pageIndex") int pageIndex) {
		return innviceServiceImpl.listOfInvoice(pageSize, pageIndex);
	}
	
}
