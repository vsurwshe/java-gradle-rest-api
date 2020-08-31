package com.rvtech.prms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.common.EmployeeDetailForProjDto;
import com.rvtech.prms.common.ExpenseDto;
import com.rvtech.prms.common.ProjectDto;
import com.rvtech.prms.common.ProjectEmployeMappingDto;
import com.rvtech.prms.services.ExpenceServiceImpl;
import com.rvtech.prms.services.ProjectEmployeMappingImpl;
import com.rvtech.prms.services.ProjectServiceImpl;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ExpenceServiceImpl expenceServiceImpl;

	@Autowired
	private ProjectEmployeMappingImpl projectEmployeMappingImpl;

	@Autowired
	private ProjectServiceImpl projectServiceImpl;

	@PostMapping(path = "/createExp", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createExp(@RequestBody List<ExpenseDto> expenseDtoList) {
		return expenceServiceImpl.create(expenseDtoList);
	}

	/*
	 * Searching based on search expenseType based on search term
	 */
	@GetMapping(path = { "/expenseList/{pageIndex}/{pageSize}",
			"/expenseList/{pageIndex}/{pageSize}/{searchTerm:.+}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> expenseList(@PathVariable("searchTerm") Optional<String> searchTerm,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		return expenceServiceImpl.expenseList(pageIndex, pageSize, searchTerm.isPresent() ? searchTerm.get() : "");
	}
	
	@GetMapping(path = "/deleteExp/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteExp(@PathVariable("id") String id) {
		return expenceServiceImpl.delete(id);

	}


	@PostMapping(path = "/createProj", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createProj(@RequestBody ProjectDto projectDto) {
		return projectServiceImpl.create(projectDto);
	}

	/*
	 * Searching based on search expenseType based on search term
	 */
	@GetMapping(path = { "/projectList/{pageIndex}/{pageSize}",
			"/projectList/{pageIndex}/{pageSize}/{searchTerm:.+}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> projectList(@PathVariable("searchTerm") Optional<String> searchTerm,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		return projectServiceImpl.projectList(pageIndex, pageSize, searchTerm.isPresent() ? searchTerm.get() : "");
	}

	@PostMapping(path = "/addEmployee", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addEmployee(@RequestBody ProjectEmployeMappingDto projectEmployeMappingDto) {
		return projectEmployeMappingImpl.create(projectEmployeMappingDto);
	}
	
	@PostMapping(path = "/editEmployee", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editEmployee(@RequestBody EmployeeDetailForProjDto employeeDetailForProjDto) {
		return projectEmployeMappingImpl.editEmmployee(employeeDetailForProjDto);
	}

	@GetMapping(path = "/delete/{proId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable("proId") String proId) {
		return projectServiceImpl.delete(proId);

	}

	@GetMapping(path = "/deleteEmployee/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") String id) {
		return projectEmployeMappingImpl.deleteEmployee(id);

	}
	
	
	/*
	 * Searching based on search expenseType based on search term
	 */
	@GetMapping(path = { "/employeeList/{pageIndex}/{pageSize}",
			"/employeeList/{pageIndex}/{pageSize}/{id:.+}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> employeeList(@PathVariable("id") Optional<String> searchTerm,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		return projectEmployeMappingImpl.employeeList(pageIndex, pageSize, searchTerm.isPresent() ? searchTerm.get() : "");
	}

}
