package com.rvtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.common.RegistrationDto;
import com.rvtech.services.AccountServiceImpl;

import io.swagger.annotations.Api;
@Api
@RestController
@RequestMapping("/registration")
public class RegistrationController {

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@PostMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registration(@RequestBody RegistrationDto registrationDto) {
		return accountServiceImpl.createUser(registrationDto);
	}

	@RequestMapping(value = "getJson", method = RequestMethod.GET)
	public @ResponseBody RegistrationDto getJson() {
		return new RegistrationDto();
	}
}
