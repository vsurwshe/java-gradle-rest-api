package com.rvtech.prms.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.rvtech.prms.common.ExpenseDto;
import com.rvtech.prms.common.MasterDataDto;
import com.rvtech.prms.common.ProjectDto;
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.entity.ExpenseEntity;
import com.rvtech.prms.entity.ProjectEntity;
import com.rvtech.prms.repository.ProjectRepository;
import com.rvtech.prms.util.GenericMapper;
import com.rvtech.prms.util.Utilities;

@Transactional
@Service
public class ProjectServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	static ModelMapper modelMapper = new ModelMapper();

	private MultiValueMap<String, String> headers = null;

	private Map<String, Object> responceMap;

	@Autowired
	private ProjectRepository projectRepository;

	public ResponseEntity<?> create(ProjectDto projectDto) {
		ProjectEntity projectEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			projectEntity = GenericMapper.mapper.map(projectDto, ProjectEntity.class);
			projectEntity = projectRepository.save(projectEntity);
			if (projectEntity.getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Expens Detail created successfully");
				responceMap.put("Id", projectEntity.getId());
				responceMap.put("Status", HttpStatus.OK);
			} else {
				headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
				headers.add(Constants.MESSAGE, "Something went wrong");
				responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ExpenceServiceImpl::create::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ResponseEntity<List<ProjectDto>> projectList(int pageIndex, int pageSize, String projectName) {
		List<ProjectDto> list = new ArrayList<ProjectDto>();
		List<ProjectEntity> projEntitylist;
		Pageable page = PageRequest.of(pageIndex, pageSize);
		headers = Utilities.getDefaultHeader();
		if (projectName == null || projectName.equals("")) {
			projEntitylist = projectRepository.findAllByActive(true, page);
		} else {
			projEntitylist = projectRepository.findAllByProjectNameOrClientNameContainingOrClientIdContainingAndActive(
					projectName, projectName, projectName, true, page);
		}

		for (ProjectEntity projectEntity : projEntitylist) {
			// ExpenseDto expenseDto=new ExpenseDto();
			ProjectDto projectDto = GenericMapper.mapper.map(projectEntity, ProjectDto.class);
			list.add(projectDto);
		}
		if (list != null && list.isEmpty()) {
			headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
			headers.add(Constants.MESSAGE, "No Data Found For SearchTerm");
		} else {
			headers.add(Constants.STATUS, HttpStatus.OK.toString());
			headers.add(Constants.MESSAGE, "Document Found Successfully For SearchTerm");
		}
		return new ResponseEntity<>(list, headers, HttpStatus.OK);
	}

	public ResponseEntity<?> delete(String proId) {
		List<ExpenseDto> list = new ArrayList<ExpenseDto>();
		List<ExpenseEntity> expEntitylist;
		headers = Utilities.getDefaultHeader();
		int update = 0;
		responceMap = new HashMap<String, Object>();
		try {
			update = projectRepository.updateActive(proId, false);
			if (update > 0) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Project deleted successfully");
				responceMap.put("Status", HttpStatus.OK);
			} else {
				headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
				headers.add(Constants.MESSAGE, "No data available");
				responceMap.put("Status", HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::delete::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);
	}

}
