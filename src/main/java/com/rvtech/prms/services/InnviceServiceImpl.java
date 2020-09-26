package com.rvtech.prms.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.rvtech.prms.common.ClientDetailDto;
import com.rvtech.prms.common.DBClientDataDto;
import com.rvtech.prms.common.FilterDto;
import com.rvtech.prms.common.InnvoiceDto;
import com.rvtech.prms.common.InvoiceDetailDto;
import com.rvtech.prms.common.InvoiceGenDto;
import com.rvtech.prms.common.InvoiceListDTO;
import com.rvtech.prms.common.InvoicePDFDto;
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.entity.AddressDetailsEntity;
import com.rvtech.prms.entity.AttendanceEntity;
import com.rvtech.prms.entity.ClientDetailsEntity;
import com.rvtech.prms.entity.InvoiceDetailEntity;
import com.rvtech.prms.entity.InvoiceEntity;
import com.rvtech.prms.entity.InvoicePDFEntity;
import com.rvtech.prms.entity.ProjectEmployeMappingEntity;
import com.rvtech.prms.entity.ProjectEntity;
import com.rvtech.prms.entity.PurchaseOrderEntity;
import com.rvtech.prms.repository.AddressRepository;
import com.rvtech.prms.repository.AttendanceRepository;
import com.rvtech.prms.repository.ClientDetailRepository;
import com.rvtech.prms.repository.ExpenceRepository;
import com.rvtech.prms.repository.InvoiceDetailRepository;
import com.rvtech.prms.repository.InvoicePDFRepository;
import com.rvtech.prms.repository.InvoiceRepository;
import com.rvtech.prms.repository.ProjectEmployeMappingRepository;
import com.rvtech.prms.repository.ProjectRepository;
import com.rvtech.prms.repository.PurchaseOrderRepository;
import com.rvtech.prms.repository.RateCardRepository;
import com.rvtech.prms.util.GenericMapper;
import com.rvtech.prms.util.Utilities;

@Transactional
@Service
public class InnviceServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(InnviceServiceImpl.class);

	static ModelMapper modelMapper = new ModelMapper();

	private MultiValueMap<String, String> headers = null;

	private Map<String, Object> responceMap;

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private ExpenceRepository expenceRepository;

	@Autowired
	private ClientDetailRepository clientDetailRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ProjectEmployeMappingRepository projectEmployeMappingRepository;

	@Autowired
	private InvoicePDFRepository invoicePDFRepository;

	@Autowired
	private RateCardRepository rateCardRepository;

	private List<ProjectEmployeMappingEntity> projEmplList;

	private List<ProjectEntity> projList;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	SimpleDateFormat dateFormatAtt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private InvoiceRepository innvoiseRepository;

	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;

	@Autowired
	private ProjectRepository projectRepository;

	private List<InvoiceDetailEntity> invoiceDetailEntities;

	private List<InvoiceDetailDto> invoiceDetailDtos;

	private InvoiceDetailEntity invoiceDetailEntity;

	private Map<Integer, Float> daypermon;

	private Calendar calendar = Calendar.getInstance();

	private int monDiff;

	public ResponseEntity<?> projectWiseBill(FilterDto filterDto) {
		DBClientDataDto dBClientDataDto = new DBClientDataDto();
		List<Map<String, String>> clientId = new ArrayList<Map<String, String>>();
		List<Map<String, Float>> clientData = new ArrayList<Map<String, Float>>();
		Map<String, Float> projectWiseBill;
		daypermon = new HashMap<Integer, Float>();
		InvoiceEntity innvoiseEntity = null;
		Date onboardingDate;
		Date exitDate;
		Float projectCost = (float) 0;
		Float rateCost = null;
		float innCost = 0;
		int dayPresent = 0;
		int halfDay = 0;
		// int toMon = calendar.get(Calendar.MONTH) + 1;
		// monDiff = toMon - startMon;

		ProjectEmployeMappingEntity projectEmployeMappingEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();

		try {
			projList = new ArrayList<ProjectEntity>();
			projList = projectRepository.findAllByActive(true);
			for (ProjectEntity obj : projList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", (String) obj.getProjectName());
				map.put("value", (String) obj.getId());
				clientId.add(map);
			}
			dBClientDataDto.setClientId(clientId);

		} catch (Exception e) {
			logger.error("InnviceServiceImpl::create::Fetching employeeList on projectId" + e.getMessage());
		}

		if (projList != null && !projList.isEmpty()) {

			invoiceDetailEntities = new ArrayList<InvoiceDetailEntity>();
			for (ProjectEntity projecEntity : projList) {
				innCost = 0;
				projectWiseBill = new HashMap<String, Float>();
				try {
					projectWiseBill.put(projecEntity.getId(), (float) 0);
					projEmplList = new ArrayList<ProjectEmployeMappingEntity>();
					projEmplList = projectEmployeMappingRepository.findAllByProjectId(projecEntity.getId());
					// Calculating per day rate of employee based on information
					for (ProjectEmployeMappingEntity projecEmplMapping : projEmplList) {
						dayPresent = 0;
						halfDay = 0;
						List<String> rateObj = rateCardRepository.findRateById(projecEmplMapping.getRateCardId());
						String rate = rateObj.get(0);

						if (String.valueOf(rate).split(",")[2].equals("Daily")) {
							rateCost = Float.valueOf(String.valueOf(rate).split(",")[0]);
						} else if (rateObj.equals("Hourly")) {
							rateCost = Float.valueOf(String.valueOf(rate).split(",")[0])
									* Float.valueOf(String.valueOf(rate).split(",")[1]);
						} else {
							rateCost = Float.valueOf(String.valueOf(rate).split(",")[0])
									/ Float.valueOf(String.valueOf(rate).split(",")[1]);
						}

						onboardingDate = projecEmplMapping.getOnbordaingDate();
						exitDate = projecEmplMapping.getExitDate() == null ? new Date()
								: projecEmplMapping.getExitDate();
						List<AttendanceEntity> attendanceEntities = attendanceRepository.findattendance(
								projecEmplMapping.getAccountId(), dateFormatAtt.format(onboardingDate),
								dateFormatAtt.format(exitDate));

						for (AttendanceEntity attendanceEntity : attendanceEntities) {
							String[] splitPresentDayAray = attendanceEntity.getDayPresent().split(",");
							for (int i = 1; i < splitPresentDayAray.length; i++) {
								if (splitPresentDayAray[i] != null && !splitPresentDayAray[i].equals(" ")
										&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate
												.getTime()
										&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate.getTime()) {
									dayPresent++;
								}
							}
							innCost = (dayPresent * rateCost) + innCost;
							String[] splitHalfDayAray = attendanceEntity.getHalfDay().split(",");
							for (int i = 1; i < splitHalfDayAray.length; i++) {
								if (splitHalfDayAray[i] != null && !splitHalfDayAray[i].equals(" ")
										&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate
												.getTime()
										&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate.getTime()) {
									halfDay++;
									calendar.setTime(dateFormat.parse(splitPresentDayAray[i]));
									daypermon.put(calendar.get(Calendar.MONTH) + 1,
											(daypermon.get(calendar.get(Calendar.MONTH) + 1) + (float) 0.5));
								}
							}
							innCost = (((halfDay / 2) + (halfDay % 2)) * rateCost) + innCost;

						}
					}

				} catch (Exception exception) {
					logger.error("projectWiseBill" + exception.getMessage());
				}
				projectWiseBill.put(projecEntity.getId(), innCost);
				clientData.add(projectWiseBill);
			}
			dBClientDataDto.setClientData(clientData);

		}

		try {
		} catch (Exception e) {
			logger.error("ExpenceServiceImpl::create::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(dBClientDataDto, headers, HttpStatus.OK);
	}

	public ResponseEntity<?> clientWiseBill(FilterDto filterDto) {
		DBClientDataDto dBClientDataDto = new DBClientDataDto();
		List<Map<String, String>> clientId = new ArrayList<Map<String, String>>();
		List<Map<String, Float>> clientData = new ArrayList<Map<String, Float>>();
		Map<String, Float> projectWiseBill = null;
		List<ClientDetailsEntity> clientList;
		daypermon = new HashMap<Integer, Float>();
		InvoiceEntity innvoiseEntity = null;
		Date onboardingDate;
		Date exitDate;
		Float projectCost = (float) 0;
		Float rateCost = null;
		float innCost = 0;
		int dayPresent = 0;
		int halfDay = 0;
		List<Object[]> clientObject = null;
		ProjectEmployeMappingEntity projectEmployeMappingEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();

		try {
			clientList = new ArrayList<ClientDetailsEntity>();
			clientObject = clientDetailRepository.allclient();
			for (Object[] obj : clientObject) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", (String) obj[0]);
				map.put("value", (String) obj[1]);
				clientId.add(map);
			}
			dBClientDataDto.setClientId(clientId);

		} catch (Exception e) {
			logger.error("InnviceServiceImpl::create::Fetching employeeList on projectId" + e.getMessage());
		}
		if (clientObject != null && !clientObject.isEmpty()) {
			for (Object[] obj : clientObject) {
				projectWiseBill = new HashMap<String, Float>();

				projList = new ArrayList<ProjectEntity>();
				projList = projectRepository.findByClientId((String) obj[1]);
				innCost = 0;

				for (ProjectEntity projecEntity : projList) {
					try {
						// projectWiseBill.put(projecEntity.getId(), (float) 0);
						projEmplList = new ArrayList<ProjectEmployeMappingEntity>();
						projEmplList = projectEmployeMappingRepository.findAllByProjectId(projecEntity.getId());
						// Calculating per day rate of employee based on information
						for (ProjectEmployeMappingEntity projecEmplMapping : projEmplList) {
							dayPresent = 0;
							halfDay = 0;
							List<String> rateObj = rateCardRepository.findRateById(projecEmplMapping.getRateCardId());
							String rate = rateObj.get(0);

							if (String.valueOf(rate).split(",")[2].equals("Daily")) {
								rateCost = Float.valueOf(String.valueOf(rate).split(",")[0]);
							} else if (rateObj.equals("Hourly")) {
								rateCost = Float.valueOf(String.valueOf(rate).split(",")[0])
										* Float.valueOf(String.valueOf(rate).split(",")[1]);
							} else {
								rateCost = Float.valueOf(String.valueOf(rate).split(",")[0])
										/ Float.valueOf(String.valueOf(rate).split(",")[1]);
							}

							onboardingDate = projecEmplMapping.getOnbordaingDate();
							exitDate = projecEmplMapping.getExitDate() == null ? new Date()
									: projecEmplMapping.getExitDate();
							List<AttendanceEntity> attendanceEntities = attendanceRepository.findattendance(
									projecEmplMapping.getAccountId(), dateFormatAtt.format(onboardingDate),
									dateFormatAtt.format(exitDate));

							for (AttendanceEntity attendanceEntity : attendanceEntities) {
								String[] splitPresentDayAray = attendanceEntity.getDayPresent().split(",");
								for (int i = 1; i < splitPresentDayAray.length; i++) {
									if (splitPresentDayAray[i] != null && !splitPresentDayAray[i].equals(" ")
											&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate
													.getTime()
											&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate
													.getTime()) {
										dayPresent++;
									}
								}
								innCost = (dayPresent * rateCost) + innCost;
								String[] splitHalfDayAray = attendanceEntity.getHalfDay().split(",");
								for (int i = 1; i < splitHalfDayAray.length; i++) {
									if (splitHalfDayAray[i] != null && !splitHalfDayAray[i].equals(" ")
											&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate
													.getTime()
											&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate
													.getTime()) {
										halfDay++;
										calendar.setTime(dateFormat.parse(splitPresentDayAray[i]));
										daypermon.put(calendar.get(Calendar.MONTH) + 1,
												(daypermon.get(calendar.get(Calendar.MONTH) + 1) + (float) 0.5));
									}
								}
								innCost = (((halfDay / 2) + (halfDay % 2)) * rateCost) + innCost;

							}
						}

					} catch (Exception exception) {
						logger.error("projectWiseBill" + exception.getMessage());
					}

				}
				projectWiseBill.put((String) obj[1], innCost);
				clientData.add(projectWiseBill);
			}
			dBClientDataDto.setClientData(clientData);

		}

		try {
		} catch (Exception e) {
			logger.error("ExpenceServiceImpl::create::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(dBClientDataDto, headers, HttpStatus.OK);
	}
	
	
	

	public ResponseEntity<?> create(InnvoiceDto innvoiceDto) {

		invoiceDetailDtos = new ArrayList<InvoiceDetailDto>();
		daypermon = new HashMap<Integer, Float>();
		
		InvoiceEntity innvoiseEntity;
		Long startTime = innvoiceDto.getFromDate().getTime();
		Long endTime = innvoiceDto.getToDate().getTime();
	
		Float innCost = (float) 0;
		Float rateCost = null;
		
		calendar.setTime(innvoiceDto.getFromDate());
		int startMon = calendar.get(Calendar.MONTH) + 1;
		calendar.setTime(innvoiceDto.getToDate());
		int toMon = calendar.get(Calendar.MONTH) + 1;
		monDiff = toMon - startMon;

		ProjectEmployeMappingEntity projectEmployeMappingEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();

		// this area finding employee by project id
		try {
			projEmplList = new ArrayList<ProjectEmployeMappingEntity>();
			projEmplList = projectEmployeMappingRepository.findAllByProjectId(innvoiceDto.getProjectId());
		} catch (Exception e) {
			logger.error("InnviceServiceImpl::create::Fetching employeeList on projectId" + e.getMessage());
		}
		
		// start the calculation invoice details 
		try {
			
			invoiceDetailEntities = new ArrayList<InvoiceDetailEntity>();
			
			for (ProjectEmployeMappingEntity employeMappingEntity : projEmplList) {
				Date onboardingDate = employeMappingEntity.getOnbordaingDate() == null ? innvoiceDto.getFromDate()
						: employeMappingEntity.getOnbordaingDate();
				Date exitDate = employeMappingEntity.getExitDate() == null ? innvoiceDto.getToDate()
						: employeMappingEntity.getExitDate();
				innCost = (float) 0;
				for (int i = startMon; i <= toMon; i++) {
					daypermon.put(i, (float) 0);
				}
				invoiceDetailEntity = new InvoiceDetailEntity();
				int dayPresent = 0;
				int halfDay = 0;
				// Calculating per day rate of employee based on information
				List<String> rateObj = rateCardRepository.findRateById(employeMappingEntity.getRateCardId());
				String rate = rateObj.get(0);

				if (String.valueOf(rate).split(",")[2].equals("Daily")) {
					rateCost = Float.valueOf(String.valueOf(rate).split(",")[0]);
				} else if (rateObj.equals("Hourly")) {
					rateCost = Float.valueOf(String.valueOf(rate).split(",")[0])
							* Float.valueOf(String.valueOf(rate).split(",")[1]);
				} else {
					rateCost = Float.valueOf(String.valueOf(rate).split(",")[0])
							/ Float.valueOf(String.valueOf(rate).split(",")[1]);
				}

				List<AttendanceEntity> attendanceEntities = attendanceRepository.findattendance(
						employeMappingEntity.getAccountId(), dateFormatAtt.format(innvoiceDto.getFromDate()),
						dateFormatAtt.format(innvoiceDto.getToDate()));

				
				// iterating the attendance entity
				for (AttendanceEntity attendanceEntity : attendanceEntities) {
					String[] splitPresentDayAray = attendanceEntity.getDayPresent().split(",");
					// this for loop calculating day present
					for (int i = 0; i < splitPresentDayAray.length; i++) {
						if (splitPresentDayAray[i] != null && !splitPresentDayAray[i].equals(" ")
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= startTime
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= endTime
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate.getTime()
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate.getTime()
							) {
							dayPresent++;
							calendar.setTime(dateFormat.parse(splitPresentDayAray[i]));
							daypermon.put(calendar.get(Calendar.MONTH) + 1, daypermon.get(calendar.get(Calendar.MONTH) + 1) + 1);
						}
					}
					
					// this line calculating invoice cost
					innCost = (dayPresent * rateCost) + innCost;
					
					String[] splitHalfDayAray = attendanceEntity.getHalfDay().split(",");
					// this loop calculating half day invoice cost
					for (int i = 0; i < splitHalfDayAray.length; i++) {
						if (splitHalfDayAray[i] != null && !splitHalfDayAray[i].equals(" ")
								&& dateFormat.parse(splitHalfDayAray[i]).getTime() >= startTime
								&& dateFormat.parse(splitHalfDayAray[i]).getTime() <= endTime
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate.getTime()
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate.getTime()
							){
							halfDay++;
							calendar.setTime(dateFormat.parse(splitPresentDayAray[i]));
							daypermon.put(calendar.get(Calendar.MONTH) + 1,(daypermon.get(calendar.get(Calendar.MONTH) + 1) + (float) 0.5));
						}
					}
					innCost = (((halfDay / 2) + (halfDay % 2)) * rateCost) + innCost;
					invoiceDetailEntity.setEmployeeName(attendanceEntity.getEmployeeName());
					invoiceDetailEntity.setEmployeeId(attendanceEntity.getEmployeeId());
					invoiceDetailEntity.setPerDayRate(rateCost);
					invoiceDetailEntity.setAttendancepermonth(daypermon.toString());
					invoiceDetailEntity.setTotalAmt(innCost);
					invoiceDetailEntity.setTotalDays((float) (dayPresent + ((halfDay / 2) + (halfDay % 2))));
					
					invoiceDetailEntities.add(invoiceDetailEntity);
				}

			}
			/*
			 * // Adding expence list if (innvoiceDto.getExpList() != null &&
			 * innvoiceDto.getExpList().size() > 0) { for (String expId :
			 * innvoiceDto.getExpList()) { innCost = expenceRepository.findExpById(expId) +
			 * innCost; } } innvoiceDto.setTotalInvAmt(innCost); innvoiseEntity =
			 * innvoiseRepository.save(GenericMapper.mapper.map(innvoiceDto,
			 * InvoiceEntity.class));
			 */
			if (invoiceDetailEntities != null && !invoiceDetailEntities.isEmpty()) {
				// For validataion if invoiceDetailEntities is empty it means attendance is not
				// available for given time , deleting generated invoice too
				/*
				 * if (invoiceDetailEntities != null && invoiceDetailEntities.isEmpty()) {
				 * innvoiseRepository.delete(innvoiseEntity); headers.add(Constants.STATUS,
				 * HttpStatus.NO_CONTENT.toString()); headers.add(Constants.MESSAGE,
				 * "Attendance is not available"); responceMap.put("Status",
				 * HttpStatus.NO_CONTENT); return new ResponseEntity<>(responceMap, headers,
				 * HttpStatus.OK); }
				 */
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Innvoice generated successfully successfully");
				responceMap.put("Status", HttpStatus.OK);

				for (InvoiceDetailEntity invoiceDetailEntity : invoiceDetailEntities) {
					// invoiceDetailEntity.setInvoiceId(innvoiseEntity.getId());
					// invoiceDetailRepository.save(invoiceDetailEntity);
					invoiceDetailDtos.add(GenericMapper.mapper.map(invoiceDetailEntity, InvoiceDetailDto.class));
				}

				return new ResponseEntity<>(invoiceDetailDtos, headers, HttpStatus.OK);

			} else {
				headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
				headers.add(Constants.MESSAGE, "Something went wrong");
				responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("ExpenceServiceImpl::create::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> generatePDF(InvoiceGenDto innvoiceDto) {
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat dateFormatForYear = new SimpleDateFormat("yy");
		Calendar calendar = Calendar.getInstance();
		String clientId;
		String poId;
		InvoicePDFDto invoicePDFDto = new InvoicePDFDto();
		// Fetching Project details on projectId
		ProjectEntity projectEntity = projectRepository.findById(innvoiceDto.getProjectId()).get();

		// Fetching client entity and setting related field
		ClientDetailsEntity clientDetailsEntity = clientDetailRepository.findByIdAndActive(projectEntity.getClientId(),
				true);

		// vALIDATION TO CHECK FOR PERTICULAR PROJECT AND DATE INVOICE IS GENERATED
		if (innvoiceDto.getProjectId() != null && innvoiceDto.getFromDate() != null) {
			List<InvoicePDFEntity> invoiceEntities = invoicePDFRepository.findinvoice(
					projectEntity.getPurchaseOrderId(), dateFormatAtt.format(innvoiceDto.getFromDate()),
					dateFormatAtt.format(innvoiceDto.getToDate()));
			if (invoiceEntities != null && !invoiceEntities.isEmpty()) {
				headers.add(Constants.STATUS, HttpStatus.CONFLICT.toString());
				headers.add(Constants.MESSAGE, "Invoice is already generated for given date");
				responceMap.put("Status", HttpStatus.CONFLICT);
				responceMap.put("fromDate", dateFormat.format(invoiceEntities.get(0).getFromDate()));
				responceMap.put("toDate", dateFormat.format(invoiceEntities.get(0).getToDate()));
				return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);
			}
		}

		int nextYear = calendar.get(Calendar.YEAR) + 1;
		invoicePDFDto.setInvoiceDate(dateFormat.format(new Date()));
		invoicePDFDto.setInvoiceNo(
				"RV/" + calendar.get(Calendar.YEAR) + "-" + String.valueOf(nextYear).substring(2) + "/01");
		invoicePDFDto.setToCompanyName(clientDetailsEntity.getClientName());
		invoicePDFDto.setToGstNo(clientDetailsEntity.getGstNum());
		invoicePDFDto.setToPanNo(clientDetailsEntity.getTanNum());

		// Fetching Client address from address table
		List<AddressDetailsEntity> addressDetailsEntities = addressRepository
				.findByClientId(projectEntity.getClientId());
		invoicePDFDto.setToCompanyAddress(addressDetailsEntities.get(0).getAddressLine() + ","
				+ addressDetailsEntities.get(0).getCity() + "-" + addressDetailsEntities.get(0).getPincode());
		invoicePDFDto.setToCompanyState(addressDetailsEntities.get(0).getState());

		float sum = 0;
		if (innvoiceDto.getInvoiceDetailDtos() != null && !innvoiceDto.getInvoiceDetailDtos().isEmpty()) {
			for (InvoiceDetailDto invoiceDetailDto : innvoiceDto.getInvoiceDetailDtos()) {
				sum = sum + invoiceDetailDto.getTotalAmt();
			}
		}

		// Setting Gst amount if state code is same then cgst and sgst will be calculted
		// or igst will be calculated
		if (invoicePDFDto.getFromGstNo().substring(0, 2).equalsIgnoreCase(invoicePDFDto.getToGstNo().substring(0, 2))) {
			invoicePDFDto.setCGST(sum * 0.09);
			invoicePDFDto.setSGSTUTGS(sum * 0.09);
		} else {
			invoicePDFDto.setIGST(sum * 0.18);
		}
		invoicePDFDto.setBillWitoutGST((double) sum);
		invoicePDFDto.setBillWitGST(sum + (sum * 0.18));
		// Creating record of invoice pdf
		InvoicePDFEntity invoicePDFEntity = GenericMapper.mapper.map(invoicePDFDto, InvoicePDFEntity.class);
		invoicePDFEntity.setPoid(projectEntity.getPurchaseOrderId());
		invoicePDFEntity.setClientId(clientDetailsEntity.getId());
		invoicePDFEntity.setFromDate(innvoiceDto.getFromDate());
		invoicePDFEntity.setToDate(innvoiceDto.getToDate());
		invoicePDFEntity = invoicePDFRepository.save(invoicePDFEntity);

		// Updating balence amount in purchase order
		PurchaseOrderEntity purchaseOrderEntity = purchaseOrderRepository
				.findByIdAndActive(projectEntity.getPurchaseOrderId(), true).get();
		purchaseOrderEntity.setInvAmnt(sum);
		purchaseOrderEntity.setBalPoAmt(purchaseOrderEntity.getPoAmount() - sum);
		purchaseOrderRepository.save(purchaseOrderEntity);
		// Storing attendance details in DB
		if (innvoiceDto.getInvoiceDetailDtos() != null && !innvoiceDto.getInvoiceDetailDtos().isEmpty()) {
			for (InvoiceDetailDto invoiceDetailDto : innvoiceDto.getInvoiceDetailDtos()) {
				InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
				invoiceDetailEntity = GenericMapper.mapper.map(invoiceDetailDto, InvoiceDetailEntity.class);
				invoiceDetailEntity.setInvoiceId(invoicePDFEntity.getId());
				invoiceDetailRepository.save(invoiceDetailEntity);
			}
		}
		return new ResponseEntity<>(invoicePDFDto, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> listOfInvoice(int pageSize, int pageIndex) {
		Pageable page = PageRequest.of(pageIndex, pageSize);
		List<InvoiceListDTO> resultListOfInvoice = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			List<InvoiceEntity> tempResultInvoiceEntity = innvoiseRepository.findInvoiceList(page);
			if (tempResultInvoiceEntity.isEmpty()) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "There is no list of invoice");
				responceMap.put("Status", HttpStatus.OK);
			} else {
				resultListOfInvoice = this.setListOfInvoice(tempResultInvoiceEntity);
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "List of invoice featched successfully");
				return new ResponseEntity<>(resultListOfInvoice, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("ExpenceServiceImpl::create::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(resultListOfInvoice, headers, HttpStatus.OK);
	}

	private List<InvoiceListDTO> setListOfInvoice(List<InvoiceEntity> tempResultInvoiceEntity) {
		List<InvoiceListDTO> resultInvoiceDto = new ArrayList<InvoiceListDTO>();
		for (InvoiceEntity invoiceEntity : tempResultInvoiceEntity) {
			ProjectEntity tempProjectEntity = this.getClientNameAndPersonNameByPojectId(invoiceEntity.getProjectId());
			InvoicePDFEntity tempInvoicePDFEntity= invoicePDFRepository.findByClientId(tempProjectEntity.getClientId(), invoiceEntity.getFromDate(), invoiceEntity.getToDate(), tempProjectEntity.getPurchaseOrder());
			InvoiceListDTO tempObject = new InvoiceListDTO(tempProjectEntity.getClientName(),
					invoiceEntity.getFromDate(), 
					tempInvoicePDFEntity !=null ? tempInvoicePDFEntity.getInvoiceNo() :"", 
					tempProjectEntity.getProjectManager(),
					invoiceEntity.getToDate(), 
					invoiceEntity.getTotalInvAmt(),
					tempInvoicePDFEntity !=null ? tempInvoicePDFEntity.getId() :"");
			resultInvoiceDto.add(GenericMapper.mapper.map(tempObject, InvoiceListDTO.class));
		}
		return resultInvoiceDto;
	}

	private ProjectEntity getClientNameAndPersonNameByPojectId(String projectId) {
		return projectRepository.findByIdEntity(projectId);
	}

}
