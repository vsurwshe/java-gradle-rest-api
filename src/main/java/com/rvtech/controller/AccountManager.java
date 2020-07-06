package com.rvtech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.common.SignInRequest;
import com.rvtech.services.AccountServiceImpl;

import io.swagger.annotations.Api;
@Api
@RestController
@RequestMapping("/authentication")
public class AccountManager {

	private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@PostMapping(path = "/signIn")
	public ResponseEntity<?> signIn(@RequestBody SignInRequest authDto) {
		logger.debug("Logging-In a user: ");
		System.out.println("Hi --- In Login" + authDto.getUserName());
		return accountServiceImpl.SignIn(authDto);
	}

	@GetMapping(path = "logOut")
	public ResponseEntity<?> logOut() {
		logger.debug("you are logging out:");
		return accountServiceImpl.logOut();
	}
}
