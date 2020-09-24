package com.rvtech.prms.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.rvtech.prms.common.AttendanceDto;
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.entity.AttendanceEntity;
import com.rvtech.prms.repository.AttendanceRepository;
import com.rvtech.prms.repository.UserInfoRepository;
import com.rvtech.prms.util.GenericMapper;
import com.rvtech.prms.util.Utilities;

@Transactional
@Service
public class AttendanceServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	static ModelMapper modelMapper = new ModelMapper();

	private MultiValueMap<String, String> headers = null;

	private Map<String, Object> responceMap;

	public void create(List<AttendanceDto> attendanceDtos) {
		AttendanceEntity attendanceEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			for (AttendanceDto attendanceDto : attendanceDtos) {
				if (attendanceDto.getEmployeeName() != null) {
					attendanceDto.setAccountId(userInfoRepository.findAccountIdByEmpId(attendanceDto.getEmployeeId()));
					attendanceEntity = GenericMapper.mapper.map(attendanceDto, AttendanceEntity.class);
					attendanceEntity = attendanceRepository.save(attendanceEntity);
				}
			}
			if (attendanceEntity.getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Attendance Detail created successfully");
				responceMap.put("Id", attendanceEntity.getId());
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

	}
}
