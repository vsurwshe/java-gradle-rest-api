package com.rvtech.prms.services;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import com.rvtech.prms.common.AddressDto;
import com.rvtech.prms.common.GraphDto;
import com.rvtech.prms.common.BankDetailsDto;
import com.rvtech.prms.common.ClientDetailDto;
import com.rvtech.prms.common.ContactPersonDto;
import com.rvtech.prms.common.DBClientDataDto;
import com.rvtech.prms.common.FilterDto;
import com.rvtech.prms.common.MasterDataDto;
import com.rvtech.prms.common.PurchaseOrderDto;
import com.rvtech.prms.common.RateCardDto;
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.controller.DashBoardController;
import com.rvtech.prms.entity.AddressDetailsEntity;
import com.rvtech.prms.entity.BankDetailsEntity;
import com.rvtech.prms.entity.ClientDetailsEntity;
import com.rvtech.prms.entity.ContactPersonEntity;
import com.rvtech.prms.entity.PurchaseOrderEntity;
import com.rvtech.prms.entity.RateCardEntity;
import com.rvtech.prms.repository.AddressRepository;
import com.rvtech.prms.repository.BankDetailsRepository;
import com.rvtech.prms.repository.ClientDetailRepository;
import com.rvtech.prms.repository.ContactPersonRepository;
import com.rvtech.prms.repository.RateCardRepository;
import com.rvtech.prms.util.GenericMapper;
import com.rvtech.prms.util.Utilities;

@Transactional
@Service
public class ClientDetailServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ClientDetailServiceImpl.class);

	static ModelMapper modelMapper = new ModelMapper();

	private MultiValueMap<String, String> headers = null;

	private Map<String, Object> responceMap;

	@Autowired
	private ClientDetailRepository clientDetailRepository;

	@Autowired
	private BankDetailsRepository bankDetailsRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private RateCardRepository rateCardRepository;

	@Autowired
	private ContactPersonRepository contactPersonRepository;

	public ResponseEntity<?> delete(String clientId) {
		int update;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			update = clientDetailRepository.updateActive(clientId, false);
			if (update > 0) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Client deleted successfully");
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

	public ResponseEntity<?> readClientWiseEmployes(FilterDto filterDto) {
		DBClientDataDto dBClientDataDto = new DBClientDataDto();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, String>> clientId = new ArrayList<Map<String, String>>();
		List<Map<String, Float>> clientData = new ArrayList<Map<String, Float>>();
		int update;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		// Fetching all clients with ids
		try {
			if (filterDto == null || filterDto.getClientId() == null) {
				List<Object[]> objects = clientDetailRepository.allclient();
				for (Object[] obj : objects) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", (String) obj[0]);
					map.put("value", (String) obj[1]);
					clientId.add(map);
				}
				dBClientDataDto.setClientId(clientId);
			} else {
				ClientDetailsEntity clientDetailsEntity = clientDetailRepository
						.findByIdAndActive(filterDto.getClientId(), true);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", clientDetailsEntity.getClientName());
				map.put("value", clientDetailsEntity.getId());
				clientId.add(map);
				dBClientDataDto.setClientId(clientId);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		try {
			Calendar calendar = Calendar.getInstance();
			// To calculet how many month data have to fetch
			int endMonth = 0;
			int queryYear = 0;
			if (calendar.get(Calendar.MONTH) > 2) {
				endMonth = calendar.get(Calendar.MONTH) - 2;
				calendar.set(calendar.get(Calendar.YEAR), 3, 1);

			} else {
				endMonth = 10 + calendar.get(Calendar.MONTH);
				calendar.set(calendar.get(Calendar.YEAR) - 1, 3, 1);
			}

			for (int i = 0; i < endMonth; i++) {
				List<Object[]> objects = null;
				/*
				 * if (calendar.get(Calendar.MONTH) > 2) { queryYear = 1;// Indicate part of
				 * financial year. If financial year is 2020/2021 , then 2020 // is first part }
				 * else { queryYear = 2; }
				 */
				// if (queryYear == 1) {
				if (filterDto == null || filterDto.getClientId() == null) {
					objects = clientDetailRepository.employeeCountByClient(dateFormat.format(calendar.getTime()));
				} else {

					objects = clientDetailRepository.employeeCountByClient(filterDto.getClientId(),
							dateFormat.format(calendar.getTime()));

				}
				/*
				 * } else { if (filterDto == null || filterDto.getClientId() == null) {
				 * calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				 * objects = clientDetailRepository
				 * .employeeCountByClientFrNextFE(dateFormat.format(calendar.getTime())); } else
				 * { objects =
				 * clientDetailRepository.employeeCountByClientIdFrNextFE(filterDto.getClientId(
				 * ), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.YEAR)); } }
				 */
				Map<String, Float> map = new HashMap<String, Float>();
				map.put("xaxis", (float) calendar.get(Calendar.MONTH) + 1);
				if (objects != null) {
					for (Object[] obj : objects) {
						map.put((String) obj[0], ((BigInteger) obj[1]).floatValue());
					}
				}
				clientData.add(map);
				calendar.add(Calendar.MONTH, 1);

			}
			dBClientDataDto.setClientData(clientData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::delete::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(dBClientDataDto, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> employesAttendenceData(FilterDto filterDto) {
		List<GraphDto> attendanceData = new ArrayList<GraphDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		int update;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		// Fetching all clients with ids

		try {
			Calendar calendar = Calendar.getInstance();
			// To calculet how many month data have to fetch
			int endMonth = 0;
			int queryYear = 0;
			if (calendar.get(Calendar.MONTH) > 2) {
				endMonth = calendar.get(Calendar.MONTH) - 2;
				calendar.set(calendar.get(Calendar.YEAR), 3, 1);

			} else {
				endMonth = 10 + calendar.get(Calendar.MONTH);
				calendar.set(calendar.get(Calendar.YEAR) - 1, 3, 1);
			}

			for (int i = 0; i < endMonth; i++) {
				GraphDto attendanceFrGraphDto = new GraphDto();
				Date startDate = calendar.getTime();
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				Date endDate = calendar.getTime();
				int joinCount = clientDetailRepository.noOfEmployeJoined(dateFormat.format(startDate),
						dateFormat.format(endDate));
				int exitCount = clientDetailRepository.noOfEmployeExit(dateFormat.format(startDate),
						dateFormat.format(endDate));
				attendanceFrGraphDto.setXaxis(String.valueOf(calendar.get(Calendar.MONTH) + 1));
				attendanceFrGraphDto.setLeft(exitCount);
				attendanceFrGraphDto.setHired(joinCount);
				attendanceData.add(attendanceFrGraphDto);
				calendar.setTime(startDate);
				calendar.add(Calendar.MONTH, 1);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::delete::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(attendanceData, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> readClientWiseBilledData(FilterDto filterDto) {
		DBClientDataDto dBClientDataDto = new DBClientDataDto();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		List<Map<String, String>> clientId = new ArrayList<Map<String, String>>();
		List<Map<String, Float>> clientData = new ArrayList<Map<String, Float>>();
		int update;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		// Fetching all clients with ids
		try {
			if (filterDto == null || filterDto.getClientId() == null) {
				List<Object[]> objects = clientDetailRepository.allclient();
				for (Object[] obj : objects) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", (String) obj[0]);
					map.put("value", (String) obj[1]);
					clientId.add(map);
				}
				dBClientDataDto.setClientId(clientId);
			} else {
				ClientDetailsEntity clientDetailsEntity = clientDetailRepository
						.findByIdAndActive(filterDto.getClientId(), true);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", clientDetailsEntity.getClientName());
				map.put("value", clientDetailsEntity.getId());
				clientId.add(map);
				dBClientDataDto.setClientId(clientId);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		try {
			Calendar calendar = Calendar.getInstance();
			// calendar.set(2021, 02, 01);
			// To calculet how many month data have to fetch
			int endMonth = 0;
			int queryYear = 0;
			if (calendar.get(Calendar.MONTH) > 2) {
				endMonth = calendar.get(Calendar.MONTH) - 2;
				calendar.set(calendar.get(Calendar.YEAR), 3, 1);

			} else {
				endMonth = 10 + calendar.get(Calendar.MONTH);
				calendar.set(calendar.get(Calendar.YEAR) - 1, 3, 1);
			}

			for (int i = 0; i < endMonth; i++) {
				List<Object[]> objects = null;

				if (filterDto == null || filterDto.getClientId() == null) {
					objects = clientDetailRepository.invoiceAmountSumByClientId(calendar.get(Calendar.MONTH) + 1,
							calendar.get(Calendar.YEAR));
				} else {
					objects = clientDetailRepository.invoiceAmountSumByClientId(calendar.get(Calendar.MONTH) + 1,
							calendar.get(Calendar.YEAR), filterDto.getClientId());
				}

				Map<String, Float> map = new HashMap<String, Float>();
				map.put("xaxis", (float) calendar.get(Calendar.MONTH) + 1);
				if (objects != null) {
					for (Object[] obj : objects) {
						map.put((String) obj[0], ((Double) obj[1]).floatValue());
					}
				}
				clientData.add(map);
				calendar.add(Calendar.MONTH, 1);

			}
			dBClientDataDto.setClientData(clientData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::delete::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(dBClientDataDto, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> create(ClientDetailDto clientDetailDto) {
		List<BankDetailsEntity> bankDetailEntitySet = new ArrayList<BankDetailsEntity>();
		List<AddressDetailsEntity> addressDetailsEntities = new ArrayList<AddressDetailsEntity>();
		List<ContactPersonEntity> contactPersonEntities = new ArrayList<ContactPersonEntity>();
		List<RateCardEntity> rateCardEntities = new ArrayList<RateCardEntity>();

		ClientDetailsEntity clientDetailsEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			clientDetailsEntity = clientDetailRepository
					.save(GenericMapper.mapper.map(clientDetailDto, ClientDetailsEntity.class));
			if (clientDetailsEntity.getId() != null) {
				if (clientDetailDto.getBankDetailsDtoList() != null
						&& !clientDetailDto.getBankDetailsDtoList().isEmpty()) {
					for (BankDetailsDto bankDetailsDto : clientDetailDto.getBankDetailsDtoList()) {
						BankDetailsEntity bankDetailsEntity = new BankDetailsEntity();
						if (bankDetailsDto.getId() == null) {
							bankDetailsEntity.setAccountNumber(bankDetailsDto.getAccountNumber());
							bankDetailsEntity.setCanceledChaqueUrl(bankDetailsDto.getCanceledChaqueUrl());
							bankDetailsEntity.setIfscCode(bankDetailsDto.getIfscCode());
							bankDetailsEntity.setActive(bankDetailsDto.isActive());
							bankDetailsEntity.setClientId(clientDetailsEntity.getId());
							bankDetailsEntity.setBranchName(bankDetailsDto.getBranchName());
							bankDetailsEntity.setBankName(bankDetailsDto.getBankName());
							bankDetailsEntity.setClientId(bankDetailsDto.getClientId());
						} else {
							bankDetailsEntity.setId(bankDetailsDto.getId());
						}
						bankDetailEntitySet.add(bankDetailsEntity);
					}
				}
				bankDetailsRepository.saveAll(bankDetailEntitySet);
				if (clientDetailDto.getAddressDtos() != null && !clientDetailDto.getAddressDtos().isEmpty()) {
					for (AddressDto addressDto : clientDetailDto.getAddressDtos()) {
						AddressDetailsEntity addressDetailsEntity = new AddressDetailsEntity();
						addressDetailsEntity.setId(addressDto.getId());
						addressDetailsEntity.setPincode(addressDto.getPincode());
						addressDetailsEntity.setAddressLine(addressDto.getAddressLine());
						addressDetailsEntity.setArea(addressDto.getArea());
						addressDetailsEntity.setState(addressDto.getState());
						addressDetailsEntity.setCity(addressDto.getCity());
						addressDetailsEntity.setActive(addressDto.isActive());
						addressDetailsEntity.setClientId(clientDetailsEntity.getId());
						addressDetailsEntities.add(addressDetailsEntity);
					}
					addressRepository.saveAll(addressDetailsEntities);
				}

				if (clientDetailDto.getContactPersonDtos() != null
						&& !clientDetailDto.getContactPersonDtos().isEmpty()) {
					for (ContactPersonDto contactPersonDto : clientDetailDto.getContactPersonDtos()) {
						ContactPersonEntity contactPersonEntity = new ContactPersonEntity();

						contactPersonEntity.setId(contactPersonDto.getId());
						contactPersonEntity.setEmail(contactPersonDto.getEmail());
						contactPersonEntity.setName(contactPersonDto.getName());
						contactPersonEntity.setMobileNum(contactPersonDto.getMobileNum());
						contactPersonEntity.setActive(contactPersonDto.isActive());
						contactPersonEntity.setClientId(clientDetailsEntity.getId());
						contactPersonEntity.setRole(contactPersonDto.getRole());
						contactPersonEntities.add(contactPersonEntity);

					}
					contactPersonRepository.saveAll(contactPersonEntities);
				}

				if (clientDetailDto.getRateCardDtos() != null && !clientDetailDto.getRateCardDtos().isEmpty()) {
					System.out.println("Count Dto " + clientDetailDto.getRateCardDtos().size());
					logger.info("Count Dto " + clientDetailDto.getRateCardDtos().size());
					for (RateCardDto rateCardDto : clientDetailDto.getRateCardDtos()) {
						RateCardEntity rateCardEntity = new RateCardEntity();

						rateCardEntity.setId(rateCardDto.getId());
						rateCardEntity.setDomainName(rateCardDto.getDomainName());
						rateCardEntity.setRate(rateCardDto.getRate());
						rateCardEntity.setSkillCategory(rateCardDto.getSkillCategory());
						rateCardEntity.setSkillSet(rateCardDto.getSkillSet());
						rateCardEntity.setActive(rateCardDto.isActive());
						rateCardEntity.setFromYearOfExp(rateCardDto.getFromYearOfExp());
						rateCardEntity.setToYearOfExp(rateCardDto.getToYearOfExp());
						rateCardEntity.setClientId(clientDetailsEntity.getId());
						rateCardEntity.setRateCardType(rateCardDto.getRateCardType());
						rateCardEntity.setRateCardDuration(rateCardDto.getRateCardDuration());
						// rateCardRepository.save(rateCardEntity);
						rateCardEntities.add(rateCardEntity);

					}
					System.out.println("Count RateCard ..." + rateCardEntities.size());
					// logger.info("Count RateCard ..."+rateCardEntities.size());
					rateCardRepository.saveAll(rateCardEntities);
				}
			}
			if (clientDetailsEntity.getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "PO Detail created successfully");
				responceMap.put("Id", clientDetailsEntity.getId());
				responceMap.put("Status", HttpStatus.OK);
			} else {
				headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
				headers.add(Constants.MESSAGE, "Something went wrong");
				responceMap.put("Id", clientDetailsEntity.getId());
				responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::saveCarDeatils::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Id", clientDetailsEntity.getId());
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> read(String id) {
		ClientDetailDto clientDetailDto;
		Optional<PurchaseOrderEntity> purchaseOrderEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		ClientDetailsEntity clientDetailsEntity = null;
		try {
			clientDetailsEntity = clientDetailRepository.findByIdAndActive(id, true);
			clientDetailDto = GenericMapper.mapper.map(clientDetailsEntity, ClientDetailDto.class);
			clientDetailDto = readOtherData(clientDetailDto);
			if (clientDetailsEntity != null && clientDetailsEntity.getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Client Detail featched successfully");
				return new ResponseEntity<>(clientDetailDto, headers, HttpStatus.OK);

			} else {
				headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
				headers.add(Constants.MESSAGE, "No data available");
				responceMap.put("Status", HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::saveCarDeatils::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ClientDetailDto readOtherData(ClientDetailDto clientDetailDto) {

		try {
			List<BankDetailsEntity> bankDetailList = bankDetailsRepository.findByClientId(clientDetailDto.getId());
			List<BankDetailsDto> bankDetailsDtos = new ArrayList<BankDetailsDto>();
			if (bankDetailList != null && !bankDetailList.isEmpty()) {
				for (BankDetailsEntity bankDetailsEntity : bankDetailList) {
					BankDetailsDto bankDetailsDto = new BankDetailsDto();
					bankDetailsDto.setId(bankDetailsEntity.getId());
					bankDetailsDto.setAccountNumber(bankDetailsEntity.getAccountNumber());
					bankDetailsDto.setCanceledChaqueUrl(bankDetailsEntity.getCanceledChaqueUrl());
					bankDetailsDto.setIfscCode(bankDetailsEntity.getIfscCode());
					bankDetailsDto.setActive(bankDetailsEntity.isActive());
					bankDetailsDto.setBankName(bankDetailsEntity.getBankName());
					bankDetailsDto.setBranchName(bankDetailsEntity.getBranchName());
					bankDetailsDto.setClientId(bankDetailsEntity.getClientId());
					bankDetailsDtos.add(bankDetailsDto);
				}
				clientDetailDto.setBankDetailsDtoList(bankDetailsDtos);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			List<AddressDetailsEntity> addressEntites = addressRepository.findByClientId(clientDetailDto.getId());
			if (addressEntites != null && !addressEntites.isEmpty()) {
				List<AddressDto> addressDtos = new ArrayList<AddressDto>();
				for (AddressDetailsEntity addressDetailsEntity : addressEntites) {
					AddressDto addressDto = new AddressDto();
					addressDto.setId(addressDetailsEntity.getId());
					addressDto.setPincode(addressDetailsEntity.getPincode());
					addressDto.setAddressLine(addressDetailsEntity.getAddressLine());
					addressDto.setArea(addressDetailsEntity.getArea());
					addressDto.setCity(addressDetailsEntity.getCity());
					addressDto.setActive(addressDetailsEntity.isActive());
					addressDto.setState(addressDetailsEntity.getState());
					addressDto.setClientId(addressDetailsEntity.getClientId());

					addressDtos.add(addressDto);
				}
				clientDetailDto.setAddressDtos(addressDtos);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			List<ContactPersonEntity> contactPersonEntities = contactPersonRepository
					.findByClientId(clientDetailDto.getId());
			if (contactPersonEntities != null && !contactPersonEntities.isEmpty()) {
				List<ContactPersonDto> contactPersonDtos = new ArrayList<ContactPersonDto>();
				for (ContactPersonEntity contactPersonEntity : contactPersonEntities) {
					ContactPersonDto contactPersonDto = new ContactPersonDto();
					contactPersonDto.setId(contactPersonEntity.getId());
					contactPersonDto.setName(contactPersonEntity.getName());
					contactPersonDto.setEmail(contactPersonEntity.getEmail());
					contactPersonDto.setMobileNum(contactPersonEntity.getMobileNum());
					contactPersonDto.setActive(contactPersonEntity.isActive());
					contactPersonDto.setClientId(contactPersonEntity.getClientId());
					contactPersonDto.setRole(contactPersonEntity.getRole());
					contactPersonDtos.add(contactPersonDto);
				}
				clientDetailDto.setContactPersonDtos(contactPersonDtos);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			List<RateCardEntity> rateCardEntities = rateCardRepository.findByClientId(clientDetailDto.getId());
			if (rateCardEntities != null && !rateCardEntities.isEmpty()) {
				List<RateCardDto> rateCardDtos = new ArrayList<RateCardDto>();
				for (RateCardEntity rateCardEntity : rateCardEntities) {
					RateCardDto rateCardDto = new RateCardDto();
					rateCardDto.setId(rateCardEntity.getId());
					rateCardDto.setDomainName(rateCardEntity.getDomainName());
					rateCardDto.setSkillCategory(rateCardEntity.getSkillCategory());
					rateCardDto.setSkillSet(rateCardEntity.getSkillSet());
					rateCardDto.setActive(rateCardEntity.isActive());
					rateCardDto.setFromYearOfExp(rateCardEntity.getFromYearOfExp());
					rateCardDto.setToYearOfExp(rateCardEntity.getToYearOfExp());
					rateCardDto.setRate(rateCardEntity.getRate());
					rateCardDto.setClientId(rateCardEntity.getClientId());
					rateCardDto.setRateCardType(rateCardEntity.getRateCardType());
					rateCardDto.setRateCardDuration(rateCardEntity.getRateCardDuration());
					rateCardDtos.add(rateCardDto);
				}
				clientDetailDto.setRateCardDtos(rateCardDtos);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clientDetailDto;

	}

	public ResponseEntity<?> search(int pageIndex, int pageSize, String clientName) {
		Pageable page = PageRequest.of(pageIndex, pageSize);
		List<ClientDetailDto> dtos = new ArrayList<ClientDetailDto>();
		ClientDetailsEntity purchaseOrderEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			List<ClientDetailsEntity> clientDetailsEntities = clientDetailRepository
					.findByClientNameContaining(clientName, page);
			if (clientDetailsEntities != null && !clientDetailsEntities.isEmpty()) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "PO Detail featched successfully");
				for (ClientDetailsEntity orderEntity : clientDetailsEntities) {
					dtos.add(GenericMapper.mapper.map(orderEntity, ClientDetailDto.class));
				}
				return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
			} else {
				headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
				headers.add(Constants.MESSAGE, "No data available");
				responceMap.put("Status", HttpStatus.NO_CONTENT);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::saveCarDeatils::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Id", purchaseOrderEntity.getId());
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> listOfClien(int pageIndex, int pageSize) {
		Pageable page = PageRequest.of(pageIndex, pageSize);
		List<ClientDetailDto> dtos = new ArrayList<ClientDetailDto>();
		ClientDetailsEntity purchaseOrderEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			List<ClientDetailsEntity> clientDetailsEntities = clientDetailRepository
					.findAllByActiveTrueOrderByCreatedOnDesc(page);
			if (clientDetailsEntities != null && !clientDetailsEntities.isEmpty()) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "PO Detail featched successfully");
				for (ClientDetailsEntity orderEntity : clientDetailsEntities) {
					dtos.add(readOtherData(GenericMapper.mapper.map(orderEntity, ClientDetailDto.class)));
				}
				return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
			} else {
				headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
				headers.add(Constants.MESSAGE, "No data available");
				responceMap.put("Status", HttpStatus.NO_CONTENT);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::saveCarDeatils::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Id", purchaseOrderEntity.getId());
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

}
