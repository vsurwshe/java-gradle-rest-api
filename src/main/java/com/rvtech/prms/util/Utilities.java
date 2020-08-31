package com.rvtech.prms.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.rvtech.prms.constant.Constants;
import com.rvtech.prms.exception.FieldErrorDetails;
import com.rvtech.prms.exception.InternalServerException;
import com.rvtech.prms.exception.ResourceNotFoundException;
import com.rvtech.prms.exception.UnProcessibleException;

public class Utilities {

	private static Logger logger = LoggerFactory.getLogger(Utilities.class);

	public static final String DELIMITER = "::";

	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static String secretKey = "cpoxyPASS!@#OxygenGlobal";
	private static final String NUM = "0123456789";
	private static final String SPL_CHARS = "!@#$%^&*_=+-/";
	private static final String URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}{5,50}$";
	private static final String NUMBER_REGEX = "[0-9]*";
	private static final String PIN_LENGTH_REGEX = "^.{6,6}$";

	public static Boolean isEmpty(String stringValue) {
		Boolean isEmpty = true;
		if (stringValue != null && stringValue.trim().length() > 0) {
			if (stringValue.trim().equalsIgnoreCase("null") || stringValue.trim().equalsIgnoreCase("<NULL>")) {
				isEmpty = true;
			} else {
				isEmpty = false;
			}

		}
		return isEmpty;
	}

	public static String stringValue(String stringValue) {
		if (stringValue != null && stringValue.trim().length() > 0) {
			return stringValue.trim();
		} else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * String request ="{\r\n" +
		 * "  \"applicationId\": \"04da390e-7f95-4b73-a541-4d3c7dd0c891\",\r\n" +
		 * "  \"cardNumber\": \"iaRs0rVLJgDXb1II8ew60fdxwJmC8Mh4UVjNBU0sCNmIGk6bK7wiC2geUZr1GsNaF7NvZGNF1yHaxARCWUFjigxvQ9elg6OLD4IhiWIZPcbsJ9wNoGF9GQYo7+rly81MSsf8//DUtCJOrN7LAY2r4teOmd9Gan0oqzYFr67Bc88=\"\r\n"
		 * + "}"; request=request.trim(); System.out.println("Request:" + request);
		 * System.out.println(request.indexOf("\"applicationId\":"));
		 */

		/*
		 * String myCodeText = "SIVA"; String filePath = "e:/SivaQR.png"; String
		 * directory = "e:/"; int height=300; int width=300; int size = 250; String
		 * fileType = "png"; String charSet="UTF-8"; File myFile = new File(filePath);
		 * Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		 * hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		 */
		// createQRImage(myFile,myCodeText,size,fileType,charSet);
		/*
		 * String path = createQrcode(myCodeText,width,height,charSet,fileType);
		 * System.out.println(path); String qrText = readQRCode(path,charSet,hintMap);
		 *//*
			 * System.out.println(qrText);
			 */
		// makeRestCallAndGetResponse(null,"https://api.binance.com/api/v3/ticker/price?symbol=ETHUSDT",null,HttpMethod.GET);
		/*
		 * generateHashKey(); String secret =
		 * "aBQU8oxW8xcLoXkBCEsHMJPmKrB3z6HII1ttK1DlVM1OvbUqRfgxVR6WAC802zDz"; String
		 * message =
		 * "symbol=LTCBTC&side=BUY&type=LIMIT&timeInForce=GTC&quantity=1&price=0.00940900&recvWindow=5000&timestamp=1564397160000";
		 * System.out.println(generate(secret,message));
		 */
		// MMddHHMMss
		// HHMMss
		// MMdd
		// yyMM
		// MMdd
		List<String> l = new ArrayList();
		/*
		 * l.add("test"); l.add("add");
		 */

		final String stringparam = l.stream().map(t -> t).collect(Collectors.joining(","));
		final String[] split = stringparam.split(",");
		final List<String> strings = Arrays.asList(split);
		System.out.println(strings.contains("test"));
		System.out.println();
		/*
		 * String dateValue = convertDateToString(ZonedDateTime.now(),"MMdd");
		 * System.out.println(dateValue);
		 */
	}

	public static String storeExpDate(Date date) throws ParseException {

		Calendar calendarObj = Calendar.getInstance();
		calendarObj.setTime(date);
		calendarObj.set(Calendar.AM_PM, 2);
		calendarObj.set(Calendar.HOUR, 0);
		calendarObj.set(Calendar.MINUTE, 0);
		calendarObj.set(Calendar.SECOND, 0);
		calendarObj.set(Calendar.MILLISECOND, 0);
		calendarObj.add(Calendar.DATE, -1);
		Integer month = calendarObj.get(Calendar.MONTH);
		month = month + 1;
		Integer years = calendarObj.get(Calendar.YEAR);
		String strYears = years.toString();
		strYears = strYears.substring(strYears.length() - 2);
		DecimalFormat dformat = new DecimalFormat("00");
		String expDate = dformat.format(month).toString().concat(dformat.format(Integer.parseInt(strYears)));
		return expDate;

	}

	public static String removeMultipleSpaces(String stringData) {

		String data = Utilities.stringValue(stringData);
		if (!data.isEmpty()) {
			data = data.trim();
			data = data.replaceAll("( +)", " ");
		}
		return data;
	}

	public static MultiValueMap<String, String> getDefaultHeader() {
		MultiValueMap<String, String> headers = new HttpHeaders();
		StringBuilder exposeHeaders = new StringBuilder();
		exposeHeaders.append(Constants.STATUS);
		exposeHeaders.append(",");
		exposeHeaders.append(Constants.MESSAGE);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, exposeHeaders.toString());
		return headers;
	}

	public static ResponseEntity<?> getUnprocessableEntityWithMessage(String message) {
		MultiValueMap<String, String> headers = Utilities.getDefaultHeader();
		headers.add(Constants.STATUS, HttpStatus.UNPROCESSABLE_ENTITY.toString());
		headers.add(Constants.MESSAGE, message);
		return new ResponseEntity<>(null, headers, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	public static List<FieldErrorDetails> prepareErrorsList(Errors errors) throws BadRequestException {

		List<FieldErrorDetails> fieldErrorDetailsList = new ArrayList<>();
		errors.getAllErrors().forEach(error -> {
			FieldError fieleError = error instanceof FieldError ? ((FieldError) error) : null;
			FieldErrorDetails fieldError = new FieldErrorDetails(fieleError.getField(), fieleError.getDefaultMessage());
			fieldErrorDetailsList.add(fieldError);
		});
		return fieldErrorDetailsList;
	}

	/*
	 * public static void prepareBadRequestExceptionAndThrow(Errors errors) throws
	 * BadRequestException { // throw new //
	 * BadRequestException(e.getAllErrors().stream().map(a->a.getDefaultMessage()).
	 * collect(Collectors.joining(","))); List<FieldErrorDetails>
	 * fieldErrorDetailsList = new ArrayList<>();
	 * errors.getAllErrors().forEach(error -> { FieldError fieleError = error
	 * instanceof FieldError ? ((FieldError) error) : null; FieldErrorDetails
	 * fieldError = new FieldErrorDetails(fieleError.getField(),
	 * fieleError.getDefaultMessage()); fieldErrorDetailsList.add(fieldError); });
	 * throw new BadRequestException(fieldErrorDetailsList); }
	 */

	public static List<String> customerPreferredLoginForUsers() {

		ArrayList<String> preferredLogins = new ArrayList<>();
		preferredLogins.add(Constants.LOGIN_WITH_USER_NAME);
		preferredLogins.add(Constants.LOGIN_WITH_EMAIL_ADDRESS);
		preferredLogins.add(Constants.LOGIN_WITH_MOBILE_NUMBER);
		preferredLogins.add(Constants.LOGIN_WITH_CONSUMER_ID);

		return preferredLogins;
	}

	public static char[] generatePswd(int minLen, int maxLen, int noOfCAPSAlpha, int noOfDigits, int noOfSplChars) {
		if (minLen > maxLen)
			throw new IllegalArgumentException("Min. Length > Max. Length!");
		if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen)
			throw new IllegalArgumentException(
					"Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
		Random rnd = new Random();
		int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
		char[] pswd = new char[len];
		int index = 0;
		for (int i = 0; i < noOfCAPSAlpha; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
		}
		for (int i = 0; i < noOfDigits; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
		}
		for (int i = 0; i < noOfSplChars; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
		}
		for (int i = 0; i < len; i++) {
			if (pswd[i] == 0) {
				pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
			}
		}
		return pswd;
	}

	private static int getNextIndex(Random rnd, int len, char[] pswd) {
		int index = rnd.nextInt(len);
		while (pswd[index = rnd.nextInt(len)] != 0)
			;
		return index;
	}

	public static void notificationPush(Object object, String notificationURL) throws ResourceNotFoundException {
		ResponseEntity<?> result = null;
		try {
			if (isEmpty(notificationURL)) {
				throw new UnProcessibleException("Notification URL is empty");
			}

			if (null == object) {
				throw new UnProcessibleException("Notification Object is empty");
			}

			URI uri = URI.create(notificationURL);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> entity = new HttpEntity<Object>(object, headers);
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			if (result.getStatusCode() != HttpStatus.OK) {
				throw new InternalServerErrorException("Something went wrong, please try again later");
			}
		} catch (RestClientException e) {
			logger.error(String.format("Push Failed for the the URL:%s", notificationURL));
			throw new ResourceNotFoundException();
		} catch (Exception e) {
			logger.error(String.format("Push Failed for the the URL:%s", notificationURL));
			throw new InternalServerErrorException(e.getMessage());
		}
		logger.info("Status Code==%s", result.getStatusCode());

	}

	/*
	 * private static void createQRImage(File qrFile, String qrCodeText, int size,
	 * String fileType,String charSet) throws WriterException, IOException { //
	 * Create the ByteMatrix for the QR-Code that encodes the given String
	 * Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
	 * hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	 * QRCodeWriter qrCodeWriter = new QRCodeWriter(); BitMatrix byteMatrix =
	 * qrCodeWriter.encode(qrCodeText.getBytes(charSet).toString(),
	 * BarcodeFormat.QR_CODE, size, size, hintMap); // Make the BufferedImage that
	 * are to hold the QRCode int matrixWidth = byteMatrix.getWidth(); BufferedImage
	 * image = new BufferedImage(matrixWidth, matrixWidth,
	 * BufferedImage.TYPE_INT_RGB); image.createGraphics();
	 * 
	 * Graphics2D graphics = (Graphics2D) image.getGraphics();
	 * graphics.setColor(Color.WHITE); graphics.fillRect(0, 0, matrixWidth,
	 * matrixWidth); // Paint and save the image using the ByteMatrix
	 * graphics.setColor(Color.BLACK);
	 * 
	 * for (int i = 0; i < matrixWidth; i++) { for (int j = 0; j < matrixWidth; j++)
	 * { if (byteMatrix.get(i, j)) { graphics.fillRect(i, j, 1, 1); } } }
	 * ImageIO.write(image, fileType, qrFile);
	 * 
	 * }
	 */

	public static byte[] createQrcode(String _text) {
		String qrcodeFilePath = "";
		byte[] qrBytes = null;
		int qrcodeHeight = 300;
		int qrcodeWidth = 300;
		String qrcodeFormat = "png";
		String charSet = "UTF-8";
		try {
			String userDirectory = System.getProperty("user.dir");
			File tempDirectory = new File(userDirectory + File.separator + "qrcodes");
			if (!tempDirectory.exists()) {
				if (tempDirectory.mkdirs()) {
					logger.debug("Multiple directories are created!");
				} else {
					logger.debug("Failed to create multiple directories!");
				}
			}
			HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, charSet);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(_text, BarcodeFormat.QR_CODE, qrcodeWidth,
					qrcodeHeight, hints);

			BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_RGB);
			File qrcodeFile = new File(tempDirectory + "/" + UUID.randomUUID().toString() + "." + qrcodeFormat);
			ImageIO.write(image, qrcodeFormat, qrcodeFile);
			MatrixToImageWriter.writeToPath(bitMatrix, qrcodeFormat, qrcodeFile.toPath());
			qrcodeFilePath = qrcodeFile.getAbsolutePath();
			BufferedImage qrimage = ImageIO.read(new File(qrcodeFilePath));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(qrimage, "png", outputStream);
			outputStream.flush();
			qrBytes = outputStream.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return qrBytes;
	}

	public static String getBase64EncodedQRCode(byte[] qrCodeData) {
		String encodedString = Base64.getEncoder().encodeToString(qrCodeData);
		return encodedString;
	}

	/*
	 * public static String readQRCode(String filePath, String charset, Map hintMap)
	 * throws FileNotFoundException, IOException, NotFoundException { BinaryBitmap
	 * binaryBitmap = new BinaryBitmap(new HybridBinarizer( new
	 * BufferedImageLuminanceSource( ImageIO.read(new FileInputStream(filePath)))));
	 * Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
	 * return qrCodeResult.getText(); }
	 */

	public static byte[] extractBytes(String ImageName) throws IOException {
		// open image
		File imgPath = new File(ImageName);
		BufferedImage bufferedImage = ImageIO.read(imgPath);

		// get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

		return (data.getData());
	}

	public static byte[] convertImageURLToByteArray(URL url) {
		try {
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.connect();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(conn.getInputStream(), baos);

			return baos.toByteArray();
		} catch (IOException e) {
			logger.error("Error Occurred while Converting URL to an Image", e);
		}
		return "".getBytes();

	}

	/*
	 * public static ResponseEntity<?> makeRestCallAndGetResponse(Object object,
	 * String url, String servletPath, HttpHeaders httpHeaders, HttpMethod method) {
	 * logger.info("URL:{},servletpath:{}", url, servletPath); if (url == null ||
	 * "".equalsIgnoreCase(url)) { throw new InternalServerException("Invalid url");
	 * } ResponseEntity<?> result = null;
	 * 
	 * HttpHeaders headers = new HttpHeaders(); if (httpHeaders != null) { headers =
	 * httpHeaders; } else {
	 * headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	 * headers.setContentType(MediaType.APPLICATION_JSON); } try { String hmacValue
	 * = ""; if (HttpMethod.GET.equals(method)) { hmacValue =
	 * Utilities.generateHashKey(servletPath, AdminApplication.HMAC_VALUE); } if
	 * (HttpMethod.POST.equals(method)) { try { ObjectMapper mapper = new
	 * ObjectMapper(); String jsonString = mapper.writeValueAsString(object);
	 * hmacValue = Utilities.generateHashKey(jsonString,
	 * AdminApplication.HMAC_VALUE); } catch (JsonProcessingException ex) {
	 * ex.printStackTrace(); }
	 * 
	 * } URI uri = null; if (null == servletPath) { uri = URI.create(url); } else {
	 * uri = URI.create(url.concat(servletPath)); } // URI uri =
	 * URI.create(url.concat(servletPath)); RestTemplate restTemplate = new
	 * RestTemplate(); headers.add(Constants.HMAC_PARAM, hmacValue);
	 * HttpEntity<Object> entity = new HttpEntity<>(object, headers); result =
	 * restTemplate.exchange(uri, method, entity, Object.class); } catch
	 * (RestClientException e) { throw new
	 * InternalServerException("UnExpected Error Occurred, Please try after some time"
	 * ); } logger.debug("Status Code==>>" + result.getStatusCode()); return result;
	 * }
	 */

	public static boolean isRegexMatches(String regex, String val) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(val);

		return matcher.matches();
	}

	public static boolean isemail(String email) {

		int min = 5;
		int max = 50;
		boolean flag = true;
		if (isRegexMatches(EMAIL_REGEX, email)) {
			if ((min > email.length() || email.length() > max)) {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	public static boolean isUrl(String url) {
		return isRegexMatches(URL_REGEX, url);
	}

	public static boolean isNumber(String val) {

		return isRegexMatches(NUMBER_REGEX, val);
	}

	public static boolean isBooleanType(String val) {
		boolean isBoolean = false;

		if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false"))
			isBoolean = true;

		return isBoolean;
	}

	public static boolean pinMinMaxCheck(String val) {
		return isRegexMatches(PIN_LENGTH_REGEX, val);
	}

	/*
	 * public static List<StatusDto> getStatusList(String status) { List<StatusDto>
	 * statusDtoList = new ArrayList<>(); switch(status){ case "Y" :
	 * statusDtoList.add(new
	 * StatusDto(Constants.STATUS_ACTIVE,Constants.STATUS_ACTIVE_DESCRIPTION));
	 * statusDtoList.add(new
	 * StatusDto(Constants.STATUS_SUSPEND,Constants.STATUS_SUSPEND_DESCRIPTION));
	 * break;
	 * 
	 * case "S" : statusDtoList.add(new
	 * StatusDto(Constants.STATUS_SUSPEND,Constants.STATUS_SUSPEND_DESCRIPTION));
	 * statusDtoList.add(new
	 * StatusDto(Constants.STATUS_RESUME,Constants.STATUS_RESUME_DESCRIPTION));
	 * break;
	 * 
	 * case "N" : statusDtoList.add(new
	 * StatusDto(Constants.STATUS_DELETED,Constants.STATUS_DELETE_DESCRIPTION));
	 * break;
	 * 
	 * case "P" : statusDtoList.add(new
	 * StatusDto(Constants.STATUS_PENDING,Constants.STATUS_PENDING_DESCRIPTION));
	 * statusDtoList.add(new
	 * StatusDto(Constants.STATUS_ACTIVE,Constants.STATUS_ACTIVE_DESCRIPTION));
	 * break;
	 * 
	 * default: statusDtoList.add(new
	 * StatusDto(Constants.STATUS_ACTIVE,Constants.STATUS_ACTIVE_DESCRIPTION));
	 * statusDtoList.add(new
	 * StatusDto(Constants.STATUS_SUSPEND,Constants.STATUS_SUSPEND_DESCRIPTION));
	 * break; } return statusDtoList; }
	 */
	public static ResponseEntity getResponseEntity(Object obj, HttpStatus httpStatus, String message) {
		final MultiValueMap<String, String> header = Utilities.getDefaultHeader();
		header.add(Constants.STATUS, httpStatus.toString());
		header.add(Constants.MESSAGE, message);
		return new ResponseEntity<>(obj, header, httpStatus);
	}

	public static String generateHashKey(String message, String secret) {
		try {
			/*
			 * String secretKey =
			 * "aBQU8oxW8xcLoXkBCEsHMJPmKrB3z6HII1ttK1DlVM1OvbUqRfgxVR6WAC802zDz"; String
			 * message =
			 * "symbol=LTCBTC&side=BUY&type=LIMIT&timeInForce=GTC&quantity=1&price=0.00940900&recvWindow=5000&timestamp=1564407427000";
			 */

			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);

			String hash = Hex.encodeHexString((sha256_HMAC.doFinal(message.getBytes("UTF-8"))));
			System.out.println(hash);

			return hash;
		} catch (Exception e) {
			System.out.println("Error");
		}
		return null;
	}

	public static String generate(final String key, final String data)
			throws NoSuchAlgorithmException, InvalidKeyException {
		if (key == null || data == null)
			throw new NullPointerException();
		final Mac hMacSHA256 = Mac.getInstance("HmacSHA256");
		byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
		final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA256");
		hMacSHA256.init(secretKey);
		byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
		byte[] res = hMacSHA256.doFinal(dataBytes);
		return Base64.getEncoder().encodeToString(res);
	}

	



	public static String convertDateToString(ZonedDateTime zonedDateTime, String format) {
		String dateValueStr = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		System.out.println("Arrive : " + formatter.format(zonedDateTime));
		dateValueStr = formatter.format(zonedDateTime);

		return dateValueStr;
	}

	public static byte[] getBytesFromLocation(String path) {
		try {
			// "/home/ganapathy/Desktop/testing/abc.csv"
			File file = new File(path);
			byte[] bytesArray = new byte[(int) file.length()];

			FileInputStream fis = new FileInputStream(file);
			fis.read(bytesArray); // read file into bytes[]
			fis.close();

			return bytesArray;

		} catch (Exception e) {

		}
		return null;
	}

	public static HttpHeaders getDefaultHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
