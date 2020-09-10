package com.rvtech.prms.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectEmployeMappingDto {

	private String id;

	private String projectId;

	@Autowired
	private List<EmployeeDetailForProjDto> employeeList;

	private Boolean active;



}
