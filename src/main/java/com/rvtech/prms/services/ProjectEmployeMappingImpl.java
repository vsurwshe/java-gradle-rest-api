package com.rvtech.prms.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.rvtech.prms.common.EmployeeDetailForProjDto;
import com.rvtech.prms.common.ProjectDto;
import com.rvtech.prms.common.ProjectEmployeMappingDto;
import com.rvtech.prms.common.UserDisplayInfoDto;
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.entity.ProjectEmployeMappingEntity;
import com.rvtech.prms.entity.ProjectEntity;
import com.rvtech.prms.entity.RateCardEntity;
import com.rvtech.prms.entity.UserInfoEntity;
import com.rvtech.prms.repository.ProjectEmployeMappingRepository;
import com.rvtech.prms.repository.RateCardRepository;
import com.rvtech.prms.repository.UserInfoRepository;
import com.rvtech.prms.util.GenericMapper;
import com.rvtech.prms.util.Utilities;

@Transactional
@Service
public class ProjectEmployeMappingImpl {
	private static final Logger logger = LoggerFactory.getLogger(ProjectEmployeMappingImpl.class);

	private MultiValueMap<String, String> headers = null;

	private Map<String, Object> responceMap;

	@Autowired
	private ProjectEmployeMappingRepository projectEmployeMappingRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private RateCardRepository cardRepository;

	public ResponseEntity<?> employeeList(int pageIndex, int pageSize, String id) {
		List<EmployeeDetailForProjDto> list = new ArrayList<EmployeeDetailForProjDto>();
		List<ProjectEmployeMappingEntity> employeMappingEntities;
		Pageable page = PageRequest.of(pageIndex, pageSize);
		headers = Utilities.getDefaultHeader();
		if (id == null || id.equals("")) {
			employeMappingEntities = projectEmployeMappingRepository.findAllByActive(true, page);
		} else {
			employeMappingEntities = projectEmployeMappingRepository.findAllByActiveAndProjectIdOrRateCardId(true, id,
					id, page);
		}
		if (employeMappingEntities != null && !employeMappingEntities.isEmpty()) {
			for (ProjectEmployeMappingEntity projectEmployeMappingEntity : employeMappingEntities) {
				UserInfoEntity userInfoEntity = userInfoRepository.findByaccountIdOrEmployeeNumber(
						projectEmployeMappingEntity.getAccountId(), projectEmployeMappingEntity.getAccountId());
				EmployeeDetailForProjDto dataDto = new EmployeeDetailForProjDto();
				dataDto = GenericMapper.mapper.map(userInfoEntity, EmployeeDetailForProjDto.class);

				// Fetching rate card name
				RateCardEntity rateCardEntity = cardRepository.findById(projectEmployeMappingEntity.getRateCardId())
						.get();
				dataDto.setCategory(rateCardEntity.getDomainName());
				dataDto.setToExperience(rateCardEntity.getToYearOfExp());
				dataDto.setFromExperience(rateCardEntity.getFromYearOfExp());
				dataDto.setSkill(rateCardEntity.getSkillSet());
				dataDto.setOnbordaingDate(projectEmployeMappingEntity.getOnbordaingDate());
				dataDto.setExitDate(projectEmployeMappingEntity.getExitDate());
				dataDto.setEmploeeMappedId(projectEmployeMappingEntity.getId());
				list.add(dataDto);
			}
			headers.add(Constants.STATUS, HttpStatus.OK.toString());
			headers.add(Constants.MESSAGE, "Employee list created successfully");
			return new ResponseEntity<>(list, headers, HttpStatus.OK);
		} else {
			headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
			headers.add(Constants.MESSAGE, "Employee list created successfully");
			responceMap.put("Status", HttpStatus.NO_CONTENT);

		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<?> create(ProjectEmployeMappingDto projectEmployeMappingDto) {
		ProjectEmployeMappingEntity projectEmployeMappingEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			for (EmployeeDetailForProjDto employeeDetailForProjDto : projectEmployeMappingDto.getEmployeeList()) {
				projectEmployeMappingEntity = new ProjectEmployeMappingEntity();
				// projectEmployeMappingDto.setAccountId(employeeDetailForProjDto.getAccountId());
				/*
				 * projectEmployeMappingEntity =
				 * GenericMapper.mapper.map(projectEmployeMappingDto,
				 * ProjectEmployeMappingEntity.class);
				 */
				projectEmployeMappingEntity.setAccountId(employeeDetailForProjDto.getAccountId());
				projectEmployeMappingEntity.setActive(projectEmployeMappingDto.getActive());
				projectEmployeMappingEntity.setId(projectEmployeMappingDto.getId());
				projectEmployeMappingEntity.setProjectId(projectEmployeMappingDto.getProjectId());
				projectEmployeMappingEntity.setRateCardId(employeeDetailForProjDto.getRateCardId());
				projectEmployeMappingEntity.setOnbordaingDate(employeeDetailForProjDto.getOnbordaingDate());
				projectEmployeMappingEntity.setExitDate(employeeDetailForProjDto.getExitDate());
				projectEmployeMappingRepository.save(projectEmployeMappingEntity);
			}

			if (projectEmployeMappingEntity.getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Expens Detail created successfully");
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

	public ResponseEntity<?> editEmmployee(EmployeeDetailForProjDto employeeDetailForProjDto) {
		ProjectEmployeMappingEntity projectEmployeMappingEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();

		try {
			projectEmployeMappingEntity = projectEmployeMappingRepository
					.findById(employeeDetailForProjDto.getEmploeeMappedId()).get();
			projectEmployeMappingEntity.setRateCardId(employeeDetailForProjDto.getRateCardId());
			projectEmployeMappingEntity.setOnbordaingDate(employeeDetailForProjDto.getOnbordaingDate());
			projectEmployeMappingEntity.setExitDate(employeeDetailForProjDto.getExitDate());
			projectEmployeMappingEntity = projectEmployeMappingRepository.save(projectEmployeMappingEntity);
		} catch (Exception exception) {
			logger.error("ProjectEmployeMappingImpl::editEmmployee::" + exception.getMessage());
		}

		if (projectEmployeMappingEntity.getId() != null) {
			headers.add(Constants.STATUS, HttpStatus.OK.toString());
			headers.add(Constants.MESSAGE, "Employee Detail updated successfully");
			responceMap.put("Status", HttpStatus.OK);
		} else {
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Employee Detail update fail");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> deleteEmployee(String id) {
		headers = Utilities.getDefaultHeader();
		int update = 0;
		responceMap = new HashMap<String, Object>();

		try {
			update = projectEmployeMappingRepository.updateActive(id, false);
			if (update > 0) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Employee deleted successfully");
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
