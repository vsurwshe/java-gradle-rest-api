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
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.entity.ExpenseEntity;
import com.rvtech.prms.entity.MasterDataEntity;
import com.rvtech.prms.repository.ExpenceRepository;
import com.rvtech.prms.repository.MasterDataRepository;
import com.rvtech.prms.util.GenericMapper;
import com.rvtech.prms.util.Utilities;

@Service
public class ExpenceServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ExpenceServiceImpl.class);

	static ModelMapper modelMapper = new ModelMapper();

	private MultiValueMap<String, String> headers = null;

	private Map<String, Object> responceMap;

	@Autowired
	private ExpenceRepository expenceRepository;

	@Autowired
	private MasterDataRepository masterDataRepository;

	public ResponseEntity<?> create(List<ExpenseDto> expenseDtoList) {
		ExpenseEntity expenseEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			for (ExpenseDto expenseDto : expenseDtoList) {
				expenseEntity = GenericMapper.mapper.map(expenseDto, ExpenseEntity.class);
				expenseEntity.setProjectId(expenseDto.getProject().getId());
				expenseEntity.setExpTypeId(expenseDto.getExpType().getId());
				expenseEntity = expenceRepository.save(expenseEntity);
			}
			if (expenseEntity.getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Expens Detail created successfully");
				responceMap.put("Id", expenseEntity.getId());
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

	public ResponseEntity<List<ExpenseDto>> expenseList(int pageIndex, int pageSize, String projectId) {
		List<ExpenseDto> list = new ArrayList<ExpenseDto>();
		List<ExpenseEntity> expEntitylist;
		Pageable page = PageRequest.of(pageIndex, pageSize);
		headers = Utilities.getDefaultHeader();
		if (projectId == null || projectId.equals("")) {
			expEntitylist = expenceRepository.findAllByActive(true, page);
		} else {
			expEntitylist = expenceRepository.findAllByProjectIdAndActive(projectId, true, page);
		}

		for (ExpenseEntity expenseEntity : expEntitylist) {
			// ExpenseDto expenseDto=new ExpenseDto();
			ExpenseDto expenseDto = GenericMapper.mapper.map(expenseEntity, ExpenseDto.class);
			expenseDto.setExpType(GenericMapper.mapper.map(
					masterDataRepository.findByActiveAndId(true, expenseEntity.getExpTypeId()), MasterDataDto.class));
			list.add(expenseDto);
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

	@Transactional
	public ResponseEntity<?> delete(String expenseId) {
		List<ExpenseDto> list = new ArrayList<ExpenseDto>();
		List<ExpenseEntity> expEntitylist;
		headers = Utilities.getDefaultHeader();
		responceMap = new HashMap<String, Object>();
		int update = expenceRepository.updateActive(expenseId, false);

		if (update != 0) {
			headers.add(Constants.STATUS, HttpStatus.OK.toString());
			headers.add(Constants.MESSAGE, "Expend Successfully  deleted successfully");
			responceMap.put("Status", HttpStatus.OK);
		} else {
			headers.add(Constants.STATUS, HttpStatus.OK.toString());
			headers.add(Constants.MESSAGE, "No Data Found For SearchTerm");
			responceMap.put("Status", HttpStatus.NO_CONTENT.toString());

		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);
	}
}
