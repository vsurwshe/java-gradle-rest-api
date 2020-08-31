package com.rvtech.prms.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.services.MasterDataServiceImpl;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping
@Transactional(readOnly = false)
public class MasterDataController {

	private static final Logger logger = LoggerFactory.getLogger(MasterDataController.class);

	@Autowired
	private MasterDataServiceImpl masterDataService;

	/*
	 * Searching based on search type Type based on search term
	 */
	@GetMapping(path = { "/masterdata/{searchType:.+}/{pageIndex}/{pageSize}",
			"/masterdata/{searchType:.+}/{pageIndex}/{pageSize}/{searchTerm:.+}",
			"/masterdata/{searchType:.+}/{pageIndex}/{pageSize}/{searchTerm:.+}/{searchCategory:.+}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> search(@PathVariable("searchTerm") Optional<String> searchTerm,
			@PathVariable("searchCategory") Optional<String> searchCategory,
			@PathVariable("searchType") String searchType, @PathVariable("pageSize") int pageSize,
			@PathVariable("pageIndex") int pageIndex) {
		return masterDataService.search(searchType, pageIndex, pageSize,
				searchTerm.isPresent() ? searchTerm.get() : "");
	}

	/*
	 * Searching project Manager based based on search term
	 */
	@GetMapping(path = { "/masterdata/searchProjectMang/{pageIndex}/{pageSize}",
			"/masterdata/searchProjectMang/{pageIndex}/{pageSize}/{searchTerm:.+}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchProjectMang(@PathVariable("searchTerm") Optional<String> searchTerm,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		return masterDataService.searchProjectMang(pageIndex, pageSize,
				searchTerm.isPresent() ? searchTerm.get() : "");
	}
	
	
	/*
	 * Searching Employee based based on search term
	 */
	@GetMapping(path = { "/masterdata/searchEmployee/{pageIndex}/{pageSize}",
			"/masterdata/searchEmployee/{pageIndex}/{pageSize}/{searchTerm:.+}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchEmployee(@PathVariable("searchTerm") Optional<String> searchTerm,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		return masterDataService.searchEmployee(pageIndex, pageSize,
				searchTerm.isPresent() ? searchTerm.get() : "");
	}
}
