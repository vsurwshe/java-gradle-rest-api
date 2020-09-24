package com.rvtech.prms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rvtech.prms.common.FileDto;
import com.rvtech.prms.util.Utilities;

@RestController
@RequestMapping(path = "/file")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private AttendanceController attendanceController;

	@Autowired
	private RegistrationController registrationController;

	private String picFolderName = "PRMSImage";

	private MultiValueMap<String, String> headers = null;

	/*
	 * Uploading document byte array and returning path
	 */
	@PostMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> upload(@RequestBody List<FileDto> fileDtos) {
		String fileName = System.getProperty("user.dir");
		List<String> documentURLS = new ArrayList<String>();
		for (FileDto fileDto : fileDtos) {
			String filePath = fileName + File.separator + picFolderName + File.separator + fileDto.getDescription();
			byte[] bytes = fileDto.getContent();
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			String line = null;
			OutputStream os = null;
			try {
				os = new FileOutputStream(
						file + File.separator + fileDto.getFileName() + "." + fileDto.getContentType());
				os.write(bytes);
				System.out.println("Write bytes to file.");
				/*
				 * BufferedReader br = new BufferedReader(new FileReader(file)); while ((line =
				 * br.readLine()) != null) { System.out.println(line); } br.close();
				 */
				os.close();
				/*
				 * documentURLS.add( documentURL + fileDto.getFileName() + File.separator + date
				 * + "-" + fileDto.getDescription());
				 */
				documentURLS.add(filePath + File.separator + fileDto.getFileName() + "." + fileDto.getContentType());
			} catch (Exception e) {
				logger.error("FileUpload");
			}
		}
		if (fileDtos.get(0).getDescription().equalsIgnoreCase("attendance")) {
			attendanceController.readExcel(fileDtos.get(0).getFromAttedDate(),fileDtos.get(0).getToAttedDate(),documentURLS.get(0));
		}
		if (fileDtos.get(0).getDescription().equalsIgnoreCase("registration")) {
			registrationController.readExcel(documentURLS.get(0));
		}
		return new ResponseEntity<List<String>>(documentURLS, HttpStatus.OK);

	}

	@GetMapping("getFile/**")
	public ResponseEntity<InputStreamResource> getFile(HttpServletRequest request) {
		// String fileName = System.getProperty("user.dir");

		// .split(request.getContextPath() + "/getFile/")[1];
		InputStream is = null;
		try {
			File file = new File(request.getRequestURI().split("getFile/")[1]);
			is = new FileInputStream(file);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(is));
		} catch (Exception e) {
			logger.error("Unable to get the flag", e);
		}
		is = FileController.class.getResourceAsStream("/images/document.png");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(is));

	}

	@RequestMapping(value = "/getImage", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getImage(@RequestParam("filepath") String filepath) {
		// String fileName = System.getProperty("user.dir");
		InputStream is = null;
		try {
			File file = new File(filepath);
			is = new FileInputStream(file);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(is));
		} catch (Exception e) {
			logger.error("Unable to get the flag", e);
		}
		is = FileController.class.getResourceAsStream("/images/document.png");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(is));

	}

	@GetMapping("getRegXl")
	public ResponseEntity<?> getRegXl() {
		// String fileName = System.getProperty("user.dir");

		// .split(request.getContextPath() + "/getFile/")[1];
		headers = Utilities.getDefaultHeader();

		FileDto fileDto = new FileDto();
		InputStream is = null;
		try {
			String fileName = System.getProperty("user.dir");
			String filePath = fileName + File.separator + picFolderName + File.separator + "DemoRegistration"
					+ File.separator + "Registration.xlsx";
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			fileDto.setContent(encoded);
			fileDto.setContentType("xlsx");
			fileDto.setFileName("Registration");
			return new ResponseEntity<>(fileDto, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Unable to get the flag", e);
		}
		is = FileController.class.getResourceAsStream("/images/document.png");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(is));

	}
}
