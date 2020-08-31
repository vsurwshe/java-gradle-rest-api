package com.rvtech.prms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.common.PurchaseOrderDto;
import com.rvtech.prms.common.RegistrationDto;
import com.rvtech.prms.services.PurchaseOrderServiceImpl;

import io.swagger.annotations.Api;
@Api
@RestController
@RequestMapping("/purchaseOrder")
public class PurchseOrderController {

	private static final Logger logger = LoggerFactory.getLogger(PurchseOrderController.class);

	@Autowired
	private PurchaseOrderServiceImpl purchaseOrderServiceImpl;

	@RequestMapping(value = "getJson", method = RequestMethod.GET)
	public @ResponseBody PurchaseOrderDto getJson() {

		return new PurchaseOrderDto();
	}

	/*
	 * Reading purchase order
	 */

	@GetMapping(path = "/search/{clientName:.+}/{pageIndex}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> search(@PathVariable("clientName") String clientName,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		return purchaseOrderServiceImpl.search(pageIndex, pageSize, clientName);
	}

	@PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody PurchaseOrderDto purchaseOrderDto) {
		return purchaseOrderServiceImpl.savePODeatils(purchaseOrderDto);
	}

	@GetMapping(path = "/read/{poId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> read(@PathVariable("poId") String poId) {
		return purchaseOrderServiceImpl.read(poId);

	}
		
	@GetMapping(path = "/delete/{poId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable("poId") String poId) {
		return purchaseOrderServiceImpl.delete(poId);

	}
	
	@GetMapping(path = "/poList/{pageIndex}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> poList(@PathVariable("pageSize") int pageSize,
			@PathVariable("pageIndex") int pageIndex) {
		return purchaseOrderServiceImpl.listOfPO(pageIndex, pageSize);

	}
}
