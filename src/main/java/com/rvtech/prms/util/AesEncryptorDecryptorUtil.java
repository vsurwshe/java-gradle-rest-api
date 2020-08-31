package com.rvtech.prms.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Amit Patgaonkar
 * 
 *
 */
@SuppressWarnings("restriction")
public class AesEncryptorDecryptorUtil {

	private static final Logger logger = LoggerFactory.getLogger(AesEncryptorDecryptorUtil.class);

	public AesEncryptorDecryptorUtil() {

	}

	private static SecretKeySpec secretKey;
	private static byte[] key;
	private static String secret= "SssHhhhhhhhhhh20!!!!";

	public static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String strToEncrypt) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String decrypt(String strToDecrypt) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static void main(String[] args) {
	//	final String secretKey = "SssHhhhhhhhhhh20!!!!";

		String originalString = "9986063632";
		String encryptedString = encrypt(originalString);
		String decryptedString = decrypt("SrRJVPJy+J73yFb1pJvYBQ==");

		System.out.println(originalString);
		System.out.println(encryptedString);
		System.out.println(decryptedString);
	}

}
