package com.rvtech.prms.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.rvtech.prms.common.InnvoiceDto;
import com.rvtech.prms.common.InvoiceDetailDto;
import com.rvtech.prms.common.InvoiceGenDto;
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

		try {
			projEmplList = new ArrayList<ProjectEmployeMappingEntity>();
			projEmplList = projectEmployeMappingRepository.findAllByProjectId(innvoiceDto.getProjectId());

		} catch (Exception e) {
			logger.error("InnviceServiceImpl::create::Fetching employeeList on projectId" + e.getMessage());
		}

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
				Object[] rateObj = rateCardRepository.findRateById(employeMappingEntity.getRateCardId());
				if (rateObj[2].equals("Daily")) {
					rateCost=(Float) rateObj[0];
				} else if (rateObj[2].equals("Daily")) {
				} else {
				}
				List<AttendanceEntity> attendanceEntities = attendanceRepository.findattendance(
						employeMappingEntity.getAccountId(), dateFormatAtt.format(innvoiceDto.getFromDate()),
						dateFormatAtt.format(innvoiceDto.getToDate()));
				for (AttendanceEntity attendanceEntity : attendanceEntities) {
					String[] splitPresentDayAray = attendanceEntity.getDayPresent().split(",");
					for (int i = 0; i < splitPresentDayAray.length; i++) {
						if (splitPresentDayAray[i] != null && !splitPresentDayAray[i].equals(" ")
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= startTime
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= endTime
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate.getTime()
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate.getTime()) {
							dayPresent++;
							calendar.setTime(dateFormat.parse(splitPresentDayAray[i]));
							daypermon.put(calendar.get(Calendar.MONTH) + 1,
									daypermon.get(calendar.get(Calendar.MONTH) + 1) + 1);
						}
					}
					innCost = (dayPresent * rateCost) + innCost;
					String[] splitHalfDayAray = attendanceEntity.getHalfDay().split(",");
					for (int i = 0; i < splitHalfDayAray.length; i++) {
						if (splitHalfDayAray[i] != null && !splitHalfDayAray[i].equals(" ")
								&& dateFormat.parse(splitHalfDayAray[i]).getTime() >= startTime
								&& dateFormat.parse(splitHalfDayAray[i]).getTime() <= endTime
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() >= onboardingDate.getTime()
								&& dateFormat.parse(splitPresentDayAray[i]).getTime() <= exitDate.getTime()) {
							halfDay++;
							calendar.setTime(dateFormat.parse(splitPresentDayAray[i]));
							daypermon.put(calendar.get(Calendar.MONTH) + 1,
									(daypermon.get(calendar.get(Calendar.MONTH) + 1) + (float) 0.5));
						}
					}
					innCost = (((halfDay / 2) + (halfDay % 2)) * rateCost) + innCost;
					// String attendancePerMon=daypermon.
					invoiceDetailEntity.setEmployeeName(attendanceEntity.getEmployeeName());
					invoiceDetailEntity.setEmployeeId(attendanceEntity.getEmployeeId());
					invoiceDetailEntity.setPerDayRate(rateCost);
					invoiceDetailEntity.setAttendancepermonth(daypermon.toString());
					invoiceDetailEntity.setTotalAmt(innCost);
					invoiceDetailEntity.setTotalDays((float) (dayPresent + ((halfDay / 2) + (halfDay % 2))));
					invoiceDetailEntities.add(invoiceDetailEntity);
				}

			}
			// Adding expence list
			if (innvoiceDto.getExpList() != null && innvoiceDto.getExpList().size() > 0) {
				for (String expId : innvoiceDto.getExpList()) {
					innCost = expenceRepository.findExpById(expId) + innCost;
				}
			}
			innvoiceDto.setTotalInvAmt(innCost);
			innvoiseEntity = innvoiseRepository.save(GenericMapper.mapper.map(innvoiceDto, InvoiceEntity.class));

			if (innvoiseEntity.getId() != null) {
				// For validataion if invoiceDetailEntities is empty it means attendance is not
				// available for given time , deleting generated invoice too
				if (invoiceDetailEntities != null && invoiceDetailEntities.isEmpty()) {
					innvoiseRepository.delete(innvoiseEntity);
					headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
					headers.add(Constants.MESSAGE, "Attendance is not available");
					responceMap.put("Status", HttpStatus.NO_CONTENT);
					return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);
				}
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "Innvoice generated successfully successfully");
				responceMap.put("Status", HttpStatus.OK);
				for (InvoiceDetailEntity invoiceDetailEntity : invoiceDetailEntities) {
					invoiceDetailEntity.setInvoiceId(innvoiseEntity.getId());
					invoiceDetailRepository.save(invoiceDetailEntity);
					invoiceDetailDtos.add(GenericMapper.mapper.map(invoiceDetailEntity, InvoiceDetailDto.class));
				}
				return new ResponseEntity<>(invoiceDetailDtos, headers, HttpStatus.OK);

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
		invoicePDFRepository.save(invoicePDFEntity);

		// Updating balence amount in purchase order
		PurchaseOrderEntity purchaseOrderEntity = purchaseOrderRepository
				.findByIdAndActive(projectEntity.getPurchaseOrderId(), true).get();
		purchaseOrderEntity.setInvAmnt(sum);
		purchaseOrderEntity.setBalPoAmt(purchaseOrderEntity.getPoAmount() - sum);
		purchaseOrderRepository.save(purchaseOrderEntity);

		return new ResponseEntity<>(invoicePDFDto, headers, HttpStatus.OK);

	}

}