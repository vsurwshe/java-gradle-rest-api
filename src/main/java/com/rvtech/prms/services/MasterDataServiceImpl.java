package com.rvtech.prms.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.rvtech.prms.common.MasterDataDto;
import com.rvtech.prms.common.UserDisplayInfoDto;
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.entity.MasterDataEntity;
import com.rvtech.prms.entity.UserInfoEntity;
import com.rvtech.prms.repository.AttendanceRepository;
import com.rvtech.prms.repository.MasterDataRepository;
import com.rvtech.prms.repository.UserInfoRepository;
import com.rvtech.prms.util.GenericMapper;
import com.rvtech.prms.util.Utilities;

@Service
public class MasterDataServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(MasterDataServiceImpl.class);

	private MultiValueMap<String, String> headers = null;

	@Autowired
	private MasterDataRepository masterDataRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private AttendanceRepository attendanceRepository;

	private List<MasterDataEntity> masterTypeEntities;

	public ResponseEntity<List<MasterDataDto>> search(String searchType, int pageIndex, int pageSize,
			String searchTerm) {
		List<MasterDataDto> list = new ArrayList<MasterDataDto>();
		Pageable page = PageRequest.of(pageIndex, pageSize);
		headers = Utilities.getDefaultHeader();
		if (searchTerm == null || searchTerm.equals("")) {
			masterTypeEntities = (List<MasterDataEntity>) masterDataRepository
					.findAllByActiveAndTypeOrCategoryContaining(true, searchType, searchType, page);
		} else {
			masterTypeEntities = (List<MasterDataEntity>) masterDataRepository
					.findByActiveAndTypeOrCategoryContainingAndNameContaining(true, searchType, searchType, searchTerm,
							page);
		}

		for (MasterDataEntity masterDataEntity : masterTypeEntities) {
			MasterDataDto dataDto = new MasterDataDto();
			dataDto.setId(masterDataEntity.getId());
			dataDto.setName(masterDataEntity.getName());
			list.add(dataDto);
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

	public ResponseEntity<?> searchProjectMang(int pageIndex, int pageSize, String searchTerm) {
		List<UserDisplayInfoDto> list = new ArrayList<UserDisplayInfoDto>();
		List<UserInfoEntity> managerList;
		Pageable page = PageRequest.of(pageIndex, pageSize);
		headers = Utilities.getDefaultHeader();
		if (searchTerm == null || searchTerm.equals("")) {
			managerList = (List<UserInfoEntity>) userInfoRepository
					.findByFirstNameContainingOrLastNameContainingAndUserTypeContaining(searchTerm, searchTerm,
							"Project Manager", page);
		} else {
			managerList = (List<UserInfoEntity>) userInfoRepository
					.findByFirstNameContainingOrLastNameContainingAndUserTypeContaining(searchTerm, searchTerm,
							"Project Manager", page);
		}

		for (UserInfoEntity userInfoEntity : managerList) {
			UserDisplayInfoDto dataDto = new UserDisplayInfoDto();
			dataDto = GenericMapper.mapper.map(userInfoEntity, UserDisplayInfoDto.class);
			list.add(dataDto);
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

	public ResponseEntity<?> searchEmployee(int pageIndex, int pageSize, String searchTerm) {
		List<UserDisplayInfoDto> list = new ArrayList<UserDisplayInfoDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-mm");
		List<UserInfoEntity> managerList;
		Pageable page = PageRequest.of(pageIndex, pageSize);
		headers = Utilities.getDefaultHeader();
		if (searchTerm == null || searchTerm.equals("")) {
			managerList = (List<UserInfoEntity>) userInfoRepository
					.findByFirstNameContainingOrLastNameContainingAndUserTypeContaining(searchTerm, searchTerm,
							"Employee", page);
		} else {
			managerList = (List<UserInfoEntity>) userInfoRepository
					.findByFirstNameContainingOrLastNameContainingAndUserTypeContaining(searchTerm, searchTerm,
							"Employee", page);
		}

		for (UserInfoEntity userInfoEntity : managerList) {
			UserDisplayInfoDto dataDto = new UserDisplayInfoDto();
			dataDto = GenericMapper.mapper.map(userInfoEntity, UserDisplayInfoDto.class);
			// Searching attendance letest record for employee
			try {
				String attendanceFromToDate = attendanceRepository.findAttendanceToAndFromDate(dataDto.getAccountId());
				String[] stringArray = attendanceFromToDate != null ? attendanceFromToDate.split(",") : null;
				dataDto.setLatestAttFromDate(stringArray == null ? null : dateFormat.parse(stringArray[0]));
				dataDto.setLatestAttToDate(stringArray == null ? null : dateFormat.parse(stringArray[1]));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("Attendence search");
			}
			list.add(dataDto);
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

}
