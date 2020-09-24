package com.rvtech.prms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.common.AttendanceDto;
import com.rvtech.prms.services.AttendanceServiceImpl;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	private AttendanceDto attendanceDto;
	private List<AttendanceDto> attendanceList;
	int month = 0;
	private String dayPresent = "";
	private String weekOff = "";
	private String halfDay = " ";
	private String abscent = " ";
	int date = 0;
	int year = 0;
	Calendar calendar = Calendar.getInstance();
	int dayCount;
	// int cellCount;
	int rowCount;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	private AttendanceServiceImpl attendanceServiceImpl;

	/*
	 * Reading attendance excel
	 */
	@Async
	@GetMapping(path = "/readExcel/fromDate/toDate/{filePath:.+}")
	public void readExcel(@PathVariable("fromDate") Date fromDate, @PathVariable("toDate") Date toDate,
			@PathVariable("filePath") String filePath) {

		attendanceList = new ArrayList<AttendanceDto>();
		attendanceList.clear();
		/*
		 * String[] days; String[] weekDays= {"Mon"};
		 */

		try {
			// obtaining input bytes from a file
			FileInputStream fis = new FileInputStream(new File(filePath));
			if (filePath.substring(filePath.lastIndexOf(".") + 1).equalsIgnoreCase("xlsx")) {
				// creating Workbook instance that refers to .xlsx file
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
				attendCal(fromDate, toDate, sheet);
			} else {
				HSSFWorkbook wb = new HSSFWorkbook(fis); // creating a Sheet object to retrieve the object
				HSSFSheet sheet = wb.getSheetAt(0); // evaluating cell type
				// FormulaEvaluator formulaEvaluator =
				// wb.getCreationHelper().createFormulaEvaluator();
				attendCal(fromDate, toDate, sheet);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (attendanceList != null && attendanceList.size() != 0) {
			attendanceServiceImpl.create(attendanceList);
		}

	}

	public void attendCal(Date fromDate, Date toDate, Sheet sheet) {
		rowCount = 0; // Keep track of row
		for (Row row : sheet) // iteration over row using for each loop
		{
			// reseting calender
			if (row.getRowNum() != 0) {
				calendar.set(year, month, date);
			}
			dayPresent = " ";
			attendanceDto = new AttendanceDto();
			dayCount = 0; // Resetting dayCount to 0, for new employee
			// cellCount = 0; // keeping track of cell count
			for (Cell cell : row) // iteration over cell using for each loop
			{

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC: // field that represents numeric cell type
					// getting the value of the cell as a number
					break;
				case Cell.CELL_TYPE_STRING: // field that represents string cell type
					// Setting attendance xl fromDate and toDate
					attendanceDto.setFromDate(fromDate);
					attendanceDto.setToDate(toDate);
					// getting the value of the cell as a string
					if (rowCount == 0 && cell.getColumnIndex() == 5) {
						String[] daystart = cell.getStringCellValue().split("\n");
						date = Integer.valueOf(daystart[1].split("/")[0]) - 1;
						month = Integer.valueOf(daystart[1].split("/")[1]) - 1;
						year = calendar.get(Calendar.YEAR);
						calendar.set(calendar.get(Calendar.YEAR), Integer.valueOf(month), Integer.valueOf(date));
					}
					if (rowCount > 0) {
						if (cell.getColumnIndex() == 0) {
							attendanceDto.setEmployeeId(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 1) {
							attendanceDto.setEmployeeName(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 2) {
							attendanceDto.setBranchNmae(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 3) {
							attendanceDto.setDesignation(cell.getStringCellValue());
						}
						if (cell.getColumnIndex() == 4) {
							attendanceDto.setCompanyName(cell.getStringCellValue());
						}
						// Counting week off
						if (cell.getStringCellValue().equals("WO")) {
							calendar.add(Calendar.DATE, 1);
							weekOff = weekOff + "," + dateFormat.format(calendar.getTime());
							if (attendanceDto.getFromDate() == null) {
								attendanceDto.setFromDate(calendar.getTime());
							} // cellCount++;// increasing cell count by 1
						}
						// Counting day present
						if (cell.getStringCellValue().equals("DP") || cell.getStringCellValue().equals("WOP")
								|| cell.getStringCellValue().equals("PHP")) {
							calendar.add(Calendar.DATE, 1);
							dayPresent = dayPresent + "," + dateFormat.format(calendar.getTime());
							if (attendanceDto.getFromDate() == null) {
								attendanceDto.setFromDate(calendar.getTime());
							}
						}
						// Counting half day
						if (cell.getStringCellValue().equals("ABS/DP") || cell.getStringCellValue().equals("DP/ABS")
								|| cell.getStringCellValue().equals("PL/DP")
								|| cell.getStringCellValue().equals("DP/PL")
								|| cell.getStringCellValue().equals("PHP/PH")) {
							calendar.add(Calendar.DATE, 1);
							halfDay = halfDay + "," + dateFormat.format(calendar.getTime());
							if (attendanceDto.getFromDate() == null) {
								attendanceDto.setFromDate(calendar.getTime());
							}
						}
						// Counting leave or absent
						if (cell.getStringCellValue().equals("ABS") || cell.getStringCellValue().equals("LWP")
								|| cell.getStringCellValue().equals("PL") || cell.getStringCellValue().equals("PH")) {
							calendar.add(Calendar.DATE, 1);
							abscent = abscent + "," + dateFormat.format(calendar.getTime());
							if (attendanceDto.getFromDate() == null) {
								attendanceDto.setFromDate(calendar.getTime());
							}
						}
					}

					break;
				}

			}
			rowCount++;
			attendanceDto.setAbsent(abscent);
			attendanceDto.setHalfDay(halfDay);
			attendanceDto.setWeekOff(weekOff);
			attendanceDto.setDayPresent(dayPresent);
			if (attendanceDto.getToDate() == null) {
				attendanceDto.setToDate(calendar.getTime());
			}
			attendanceList.add(attendanceDto);

		}
	}

}
