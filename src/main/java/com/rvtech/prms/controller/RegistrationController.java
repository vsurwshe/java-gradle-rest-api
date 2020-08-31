package com.rvtech.prms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.common.AttendanceDto;
import com.rvtech.prms.common.DBClientDataDto;
import com.rvtech.prms.common.RegistrationDto;
import com.rvtech.prms.services.AccountServiceImpl;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/registration")
public class RegistrationController {

	private List<RegistrationDto> regList;

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@PostMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registration(@RequestBody RegistrationDto registrationDto) {
		return accountServiceImpl.createUser(registrationDto);
	}

	@RequestMapping(value = "getJson", method = RequestMethod.GET)
	public @ResponseBody DBClientDataDto getJson() {
		DBClientDataDto dBClientDataDto=new DBClientDataDto();
		Map<String, String> clientId=new HashMap<String, String>();
		Map<String, Float> clientData=new HashMap<String, Float>();
		/*
		 * clientId.put("RigVed Tech", "1234"); clientId.put("RIL","5678");
		 * dBClientDataDto.setClientId(clientId); clientData.put("xaxis", (float) 2.0);
		 * clientData.put("1234", (float) 35.0); clientData.put("5678", (float) 20.0);
		 * dBClientDataDto.setClientData(clientData);
		 */
		return dBClientDataDto;
	}

	/*
	 * Reading attendance excel
	 */
	@Async
	@GetMapping(path = "/readExcel/{filePath:.+}")
	public void readExcel(@PathVariable("filePath") String filePath) {

		regList = new ArrayList<RegistrationDto>();
		regList.clear();

		try {
			File file = new File(filePath); // creating a new file
											// instance
			FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
			if (filePath.substring(filePath.lastIndexOf(".") + 1).equalsIgnoreCase("xlsx")) {
				// creating Workbook instance that refers to .xlsx file
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
				readSheet(sheet);
			} else {
				HSSFWorkbook wb = new HSSFWorkbook(fis); // creating a Sheet object to retrieve the object
				HSSFSheet sheet = wb.getSheetAt(0); // evaluating cell type
				// FormulaEvaluator formulaEvaluator =
				// wb.getCreationHelper().createFormulaEvaluator();
				readSheet(sheet);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		/*
		 * String[] days; String[] weekDays= {"Mon"};
		 */

		/*
		 * try { // obtaining input bytes from a file FileInputStream fis = new
		 * FileInputStream(new File(filePath)); // creating workbook instance that
		 * refers to .xls file HSSFWorkbook wb = new HSSFWorkbook(fis); // creating a
		 * Sheet object to retrieve the object HSSFSheet sheet = wb.getSheetAt(0); //
		 * evaluating cell type FormulaEvaluator formulaEvaluator =
		 * wb.getCreationHelper().createFormulaEvaluator(); for (Row row : sheet) //
		 * iteration over row using for each loop { RegistrationDto registrationDto =
		 * new RegistrationDto();
		 * 
		 * if (row.getRowNum() > 0) { for (Cell cell : row) // iteration over cell using
		 * for each loop { switch (formulaEvaluator.evaluateInCell(cell).getCellType())
		 * { case Cell.CELL_TYPE_NUMERIC: // field that represents numeric cell type if
		 * (cell.getColumnIndex() == 11) { registrationDto.setCTC((float)
		 * cell.getNumericCellValue()); } if (cell.getColumnIndex() == 12) {
		 * registrationDto.setDOB(cell.getDateCellValue()); } if (cell.getColumnIndex()
		 * == 15) { registrationDto.setJoiningDate(cell.getDateCellValue()); } if
		 * (cell.getColumnIndex() == 14) { registrationDto.setExpInYears((float)
		 * cell.getNumericCellValue()); } if (cell.getColumnIndex() == 3) {
		 * registrationDto.setMobileNumber(String.valueOf(cell.getNumericCellValue()));
		 * } break;
		 * 
		 * case Cell.CELL_TYPE_STRING: // field that represents string cell type
		 * 
		 * if (cell.getColumnIndex() == 0) {
		 * registrationDto.setEmployeeNumber(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 1) {
		 * registrationDto.setFirstName(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 2) {
		 * registrationDto.setLastName(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 3) {
		 * registrationDto.setMobileNumber(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 4) {
		 * registrationDto.setEmailId(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 5) {
		 * registrationDto.setGender(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 6) {
		 * registrationDto.setDesignation(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 7) {
		 * registrationDto.setDomain(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 8) {
		 * registrationDto.setCompanyName(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 9) {
		 * registrationDto.setPrimerySkill(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 10) {
		 * registrationDto.setSecounderySkill(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 13) {
		 * registrationDto.setUserType(cell.getStringCellValue()); } if
		 * (cell.getColumnIndex() == 16) {
		 * registrationDto.setPassword(cell.getStringCellValue()); }
		 * 
		 * break; } }
		 * 
		 * } regList.add(registrationDto); } } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */

		for (RegistrationDto registrationDto : regList) {
			if (registrationDto.getFirstName() != null && registrationDto.getLastName() != null)
				registration(registrationDto);
		}
	}

	public void readSheet(Sheet sheet) {
		for (Row row : sheet) // iteration over row using for each loop
		{

			if (row.getRowNum() > 0) {
				RegistrationDto registrationDto = new RegistrationDto();
				for (Cell cell : row) // iteration over cell using for each loop
				{
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC: // field that represents numeric cell type
						if (cell.getColumnIndex() == 11) {
							registrationDto.setCTC((float) cell.getNumericCellValue());
						}
						if (cell.getColumnIndex() == 12) {
							registrationDto.setDOB(cell.getDateCellValue());
						}
						if (cell.getColumnIndex() == 15) {
							registrationDto.setJoiningDate(cell.getDateCellValue());
						}
						if

						(cell.getColumnIndex() == 14) {
							registrationDto.setExpInYears((float) cell.getNumericCellValue());
						}
						if (cell.getColumnIndex() == 3) {
							registrationDto.setMobileNumber(String.valueOf(cell.getNumericCellValue()));
						}
						break;

					case Cell.CELL_TYPE_STRING: // field that represents string cell type

						if (cell.getColumnIndex() == 0) {
							registrationDto.setEmployeeNumber(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 1) {
							registrationDto.setFirstName(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 2) {
							registrationDto.setLastName(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 3) {
							registrationDto.setMobileNumber(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 4) {
							registrationDto.setEmailId(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 5) {
							registrationDto.setGender(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 6) {
							registrationDto.setDesignation(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 7) {
							registrationDto.setDomain(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 8) {
							registrationDto.setCompanyName(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 9) {
							registrationDto.setPrimerySkill(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 10) {
							registrationDto.setSecounderySkill(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 13) {
							registrationDto.setUserType(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 16) {
							registrationDto.setPassword(cell.getStringCellValue());
						}

						break;
					}

				}
				regList.add(registrationDto);

			}
		}
	}
}
