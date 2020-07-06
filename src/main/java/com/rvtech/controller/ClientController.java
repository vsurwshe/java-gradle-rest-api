package com.rvtech.controller;

import java.util.Optional;

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

import com.rvtech.common.ClientDetailDto;
import com.rvtech.common.PurchaseOrderDto;
import com.rvtech.services.ClientDetailServiceImpl;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientDetailServiceImpl clientDetailServiceImpl;

	@RequestMapping(value = "getJson", method = RequestMethod.GET)
	public @ResponseBody ClientDetailDto getJson() {

		return new ClientDetailDto();
	}

	/*
	 * Reading purchase order
	 */

	@GetMapping(path = { "/searchClient/{pageIndex}/{pageSize}",
			"/searchClient/{pageIndex}/{pageSize}/{searchTerm:.+}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchClient(@PathVariable("searchTerm") Optional<String> searchTerm,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		return clientDetailServiceImpl.search(pageIndex, pageSize, searchTerm.isPresent() ? searchTerm.get() : "");
	}

	@PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody ClientDetailDto clientDetailDto) {
		return clientDetailServiceImpl.create(clientDetailDto);
	}

	@GetMapping(path = "/read/{poId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> read(@PathVariable("poId") String poId) {
		return clientDetailServiceImpl.read(poId);

	}

	@GetMapping(path = "/clientList/{pageIndex}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clientList(@PathVariable("pageSize") int pageSize,
			@PathVariable("pageIndex") int pageIndex) {
		return clientDetailServiceImpl.listOfClien(pageIndex, pageSize);

	}
}
