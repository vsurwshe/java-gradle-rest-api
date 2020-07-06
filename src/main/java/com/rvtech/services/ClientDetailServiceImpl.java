package com.rvtech.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import com.rvtech.common.AddressDto;
import com.rvtech.common.BankDetailsDto;
import com.rvtech.common.ClientDetailDto;
import com.rvtech.common.ContactPersonDto;
import com.rvtech.common.MasterDataDto;
import com.rvtech.common.PurchaseOrderDto;
import com.rvtech.common.RateCardDto;
import com.rvtech.constant.Constants;
import com.rvtech.entity.AddressDetailsEntity;
import com.rvtech.entity.BankDetailsEntity;
import com.rvtech.entity.ClientDetailsEntity;
import com.rvtech.entity.ContactPersonEntity;
import com.rvtech.entity.PurchaseOrderEntity;
import com.rvtech.entity.RateCardEntity;
import com.rvtech.repository.AddressRepository;
import com.rvtech.repository.BankDetailsRepository;
import com.rvtech.repository.ClientDetailRepository;
import com.rvtech.repository.ContactPersonRepository;
import com.rvtech.repository.RateCardRepository;
import com.rvtech.util.GenericMapper;
import com.rvtech.util.Utilities;

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

	public ResponseEntity<?> create(ClientDetailDto clientDetailDto) {
		Set<BankDetailsEntity> bankDetailEntitySet = new LinkedHashSet<BankDetailsEntity>();
		Set<AddressDetailsEntity> addressDetailsEntities = new LinkedHashSet<AddressDetailsEntity>();
		Set<ContactPersonEntity> contactPersonEntities = new LinkedHashSet<ContactPersonEntity>();
		Set<RateCardEntity> rateCardEntities = new LinkedHashSet<RateCardEntity>();

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
						contactPersonEntities.add(contactPersonEntity);

					}
					contactPersonRepository.saveAll(contactPersonEntities);
				}

				if (clientDetailDto.getRateCardDtos() != null && !clientDetailDto.getRateCardDtos().isEmpty()) {
					for (RateCardDto rateCardDto : clientDetailDto.getRateCardDtos()) {
						RateCardEntity rateCardEntity = new RateCardEntity();

						rateCardEntity.setId(rateCardDto.getId());
						rateCardEntity.setDomainName(rateCardDto.getDomainName());
						rateCardEntity.setSkillCategory(rateCardDto.getSkillCategory());
						rateCardEntity.setSkillSet(rateCardDto.getSkillSet());
						rateCardEntity.setActive(rateCardDto.isActive());
						rateCardEntity.setYearOfExp(rateCardDto.getYearOfExp());
						rateCardEntity.setClientId(clientDetailsEntity.getId());
						rateCardEntities.add(rateCardEntity);

					}
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
					rateCardDto.setYearOfExp(rateCardEntity.getYearOfExp());
					rateCardDto.setClientId(rateCardEntity.getClientId());
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
