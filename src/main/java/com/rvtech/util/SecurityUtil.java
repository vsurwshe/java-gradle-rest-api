package com.rvtech.util;

import java.util.Random;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rvtech.common.UserAccountDto;
import com.rvtech.entity.UserAccount;

/**
 * The class {@link SecurityUtil} is an helper class for the user related secure
 * information. It helps to get the user information like accounId, user
 * account, member information etc.
 * 
 * @author Sunil Kamatagi
 * 
 */
public class SecurityUtil {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	/**
	 * Returns the currently accessible {@link UserAccount} available to the calling
	 * code depending on logged in user.
	 * 
	 * @return {@link UserAccount}
	 */
	public static UserAccount getLoggedInUserAccount() {
		logger.debug("Fetcing Account Info: ");
		return GenericMapper.mapper.map(getAccountFromContext(), UserAccount.class);
	}

	/**
	 * Returns the currently accessible user accountId available to the calling code
	 * depending on logged in user.
	 * 
	 * @return accounId - The account id of a logged in user
	 */
	public static String getLoggedInAccountId() {
		logger.debug("Fetcing Account Id: ");
		UserAccountDto userAccountDto = getAccountFromContext();
		return null != userAccountDto ? userAccountDto.getAccountId() : null;
	}

	/**
	 * Returns the currently accessible {@link UserAccountDto} available to the
	 * calling code depending on logged in user.
	 * 
	 * @return {@link UserAccountDto}
	 */
	public static UserAccountDto getLoggedInUserAccountDto() {
		logger.debug("Fetcing Account Info DTO: ");
		return getAccountFromContext();
	}

	/**
	 * Returns the currently accessible {@link UserAccountDto} available to the
	 * calling code depending on logged in user.
	 * 
	 * @return {@link UserAccountDto}
	 */
	private static UserAccountDto getAccountFromContext() {
		Subject subject = SecurityUtils.getSubject();
		UserAccountDto userAccountDto = (UserAccountDto) subject.getPrincipal();
		return userAccountDto;
	}

	/*
	 * public static UserAccountMemberDto getPrimaryMember() { UserAccountDto
	 * userAccountDto = getAccountFromContext();
	 * 
	 * if (userAccountDto!=null) { List<UserAccountMemberDto> userAccountMemberDtos
	 * = userAccountDto .getUserAccountMembers(); if (null != userAccountMemberDtos
	 * && userAccountMemberDtos.size() > 0) { for (UserAccountMemberDto memberDto :
	 * userAccountMemberDtos) { if (memberDto.isPrimaryMember()) {
	 * userAccountMemberDto = memberDto; break; } } } }
	 * 
	 * if (userAccountDto != null) { UserAccountMemberDto userAccountMemberDto =
	 * userAccountDto.getUserAccountMembers(); return userAccountMemberDto; } return
	 * null; }
	 */

	/**
	 * Generate a random password.
	 * 
	 * @param passLength - passLength must be >= 10. If not some times it returns
	 *                   null as the options specified is more secured.
	 * @return
	 */
	public static String generateRandomPassword(int passLength) {
		/*
		 * PwGeneratorOptionBuilder options = new PwGeneratorOptionBuilder()
		 * .setDoNotEndWithSymbol().setDoNotStartsWithSymbol()
		 * .setIncludeNumerals(true).setNumberOfPasswords(1)
		 * .setMaxAttempts(100).setAtLeast2Digits()
		 * .setPasswordLength(passLength).setIncludeAmbiguous(false)
		 * .setIncludeSymbols(true).setUseRandom().setIncludeOneCapital(); String
		 * password = null; List<String> passwords = PwHelper.process(options.build(),
		 * null); for (Iterator<String> iterator = passwords.iterator(); iterator
		 * .hasNext();) { password = iterator.next(); }
		 * 
		 * return password;
		 */
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		return sb.toString();
	}

}
