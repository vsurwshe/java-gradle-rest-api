package com.rvtech.prms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.common.FilterDto;
import com.rvtech.prms.services.ClientDetailServiceImpl;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

	@Autowired
	private ClientDetailServiceImpl clientDetailServiceImpl;

	@PostMapping(value = "employeeDetail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> employeeDetailByClient(@RequestBody FilterDto filterDto) {
		return clientDetailServiceImpl.readClientWiseEmployes(filterDto);
	}

}
