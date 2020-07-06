package com.rvtech.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.rvtech.common.RegistrationDto;
import com.rvtech.common.SignInRequest;
import com.rvtech.common.UserAccountDto;
import com.rvtech.constant.Constants;
import com.rvtech.entity.UserAccount;
import com.rvtech.entity.UserInfoEntity;
import com.rvtech.repository.AccountRepository;
import com.rvtech.repository.UserInfoRepository;
import com.rvtech.util.AesEncryptorDecryptorUtil;
import com.rvtech.util.GenericMapper;
import com.rvtech.util.SecurityUtil;
import com.rvtech.util.Utilities;

/**
 * The actual implementation of the
 * {@link com.health5c.ccp.data.jpa.service.AccountService} interface This
 * implementation uses the
 * {@link com.health5c.ccp.data.jpa.repository.AccountRepository}
 * 
 * @author Amit Patgaonkar
 */
@Service
public class AccountServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private PasswordService userPasswordService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	private static AesEncryptorDecryptorUtil encryptorDecryptorService = new AesEncryptorDecryptorUtil();

	private MultiValueMap<String, String> headers = null;

	private Map<String, String> param;

	@Transactional
	public ResponseEntity<?> createUser(RegistrationDto registrationDto) {
		param=new HashMap<String, String>();
		UserAccountDto userAccountDto;
		try {
			userAccountDto = getByUserNameOrEmailForLogin(registrationDto.getMobileNumber());
			if (userAccountDto == null) {
				userAccountDto = getByUserNameOrEmailForLogin(registrationDto.getEmailId());
			}
			if (userAccountDto != null) {
				headers.add(Constants.STATUS, HttpStatus.CONFLICT.toString());
				headers.add(Constants.MESSAGE, "Mobile number or Email id already exist");
				param.put("Status", HttpStatus.CONFLICT.toString());
				return new ResponseEntity<>(param, headers, HttpStatus.CONFLICT);
			}
		} catch (Exception exception) {
			logger.error("AccountServiceImpl:createUser:" + exception.getMessage());
		}
		UserAccount userAccount = new UserAccount();

		UserInfoEntity userInfoEntity = new UserInfoEntity();
		try {
			String encryptedMobileNo = encryptorDecryptorService.encrypt(registrationDto.getMobileNumber());

			String encryptedEmailId = encryptorDecryptorService.encrypt(registrationDto.getEmailId());

			String encryptedPassword = userPasswordService.encryptPassword(registrationDto.getPassword());

			userAccount.setPassword(encryptedPassword);
			userAccount.setUserName(encryptedMobileNo);
			userAccount.setEmailid(encryptedEmailId);
			userAccount.setActive(true);
			userAccount.setRoleId(1l);
			userAccount = this.accountRepository.save(userAccount);

			userInfoEntity.setAccountId(userAccount.getAccountId());
			userInfoEntity.setFirstName(registrationDto.getFirstName());
			userInfoEntity.setLastName(registrationDto.getLastName());
			userInfoEntity.setMobileNumber(registrationDto.getMobileNumber());
			userInfoEntity.setEmailId(registrationDto.getEmailId());
			userInfoEntity.setCompanyName(registrationDto.getCompanyName());
			userInfoEntity.setCTC(registrationDto.getCTC());
			userInfoEntity.setDesignation(registrationDto.getDesignation());
			userInfoEntity.setDOB(registrationDto.getDOB());
			userInfoEntity.setDomain(registrationDto.getDomain());
			userInfoEntity.setPrimerySkill(registrationDto.getPrimerySkill());
			userInfoEntity.setSecounderySkill(registrationDto.getSecounderySkill());
			userInfoEntity.setEmployeeNumber(registrationDto.getEmployeeNumber());
			userInfoEntity.setUserType(registrationDto.getUserType());
			userInfoEntity = userInfoRepository.save(userInfoEntity);
			if (userInfoEntity.getMemberId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, userInfoEntity.getUserType() + "register successfully");
				param.put("Status", HttpStatus.OK.toString());
			}
		} catch (Exception exception) {
			logger.error("AccountServiceImpl:createUser:"+exception.getMessage());
		}
		return new ResponseEntity<>(param, headers, HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	public SignInRequest getAuth(String accountId, String userName) {
		SignInRequest authDto = null;
		try {
			UserAccount userAccount = accountRepository.findOneByAccountIdAndUserName(accountId, userName);
			if (null != userAccount) {
				authDto = new SignInRequest();
				authDto.setUserName(
						userAccount.getUserName() == null ? userAccount.getEmailid() : userAccount.getUserName());

				authDto.setPassword(userAccount.getPassword());
			}

		} catch (NoResultException nsre) {
			if (logger.isDebugEnabled()) {
				logger.debug("No account found for the specified user :: [", accountId, "]");
			}
			authDto = null;
		}
		return authDto;
	}

	@Transactional(readOnly = true)
	public UserAccountDto getByUserNameOrEmailForLogin(String userName) {
		logger.debug("Retrieving user by username:: [ ", userName, " ]");
		UserAccountDto userAccountDto = null;
		UserAccount userAccount = new UserAccount();
		try {

			String encryptedUserName = encryptorDecryptorService.encrypt(userName);

			try {
				userAccount = this.accountRepository.findByMobileAndEmailId(encryptedUserName);
			} catch (Exception e) {
				System.out.println("testing for login");
			}
			userAccountDto = null != userAccount ? GenericMapper.mapper.map(userAccount, UserAccountDto.class) : null;
		} catch (NoResultException nsre) {
			if (logger.isDebugEnabled()) {
				logger.debug("No account found for the specified user :: [", userName, "]");
			}
			userAccountDto = null;
		}
		return userAccountDto;
	}

	/**
	 * Gets the User using specified user id
	 * 
	 * @param id unique id assigned to the user
	 * @return <code>User</code> object; <code>null</code> if the user does not
	 *         exist in the database
	 */
	@Transactional(readOnly = true)
	public UserAccountDto getByUserId(String accountId) {
		UserAccountDto userAccountDto = null;
		try {
			Optional<UserAccount> userAccountOptional = accountRepository.findById(accountId);
			if (userAccountOptional.isPresent()) {
				// userAccountDto = AccountMapper.mapToDto(userAccountOptional.get());
				userAccountDto = GenericMapper.mapper.map(userAccountOptional.get(), UserAccountDto.class);
			}
		} catch (NoResultException nsre) {
			if (logger.isDebugEnabled()) {
				logger.debug("No account found for the specified user :: [", accountId, "]");
			}
			// userAccountDto = null;
		}
		return userAccountDto;
	}

	@Transactional(readOnly = false)
	public ResponseEntity<?> SignIn(SignInRequest request) {
		UsernamePasswordToken authenticationToken = null;
		param = new HashMap<String, String>();
		headers = Utilities.getDefaultHeader();
		Subject subject = null;
		Session session;
		Map attributes = new LinkedHashMap();

		try {

			authenticationToken = new UsernamePasswordToken(request.getUserName(), request.getPassword(),
					request.getLoginType());
			subject = SecurityUtils.getSubject();
			session = subject.getSession();
			/** retain Session attributes to put in the new session after login: **/
			Collection<Object> keys = session.getAttributeKeys();
			for (Object key : keys) {
				Object value = session.getAttribute(key);
				if (value != null)
					attributes.put(key, value);
			}
			session.stop();
		} catch (AuthenticationException e) {
			String errMsg = "Signed in Failed " + e.getMessage();
			logger.debug(errMsg);

		}
		/**
		 * this will create a new session by default in applications that allow session
		 * state:
		 **/
		try {
			subject.login(authenticationToken);
			System.out.println("Hi --- In Login" + subject.getSession().getId());
			session = subject.getSession();
			headers.add(Constants.STATUS, HttpStatus.OK.toString());
			headers.add(Constants.MESSAGE, "Successfully signed in");
			//headers.add("Access-Control-Allow-Origin", "*");
			param.put("Authorization", session.getId().toString());
			param.put("AccountId", SecurityUtil.getLoggedInAccountId());
		} catch (AuthenticationException e) {
			headers.add(Constants.STATUS, HttpStatus.UNAUTHORIZED.toString());
			headers.add(Constants.MESSAGE, "Credentials are not correct");
		} catch (Exception ex) {
			headers.add(Constants.STATUS, HttpStatus.UNAUTHORIZED.toString());
			headers.add(Constants.MESSAGE, "Credentials are not correct");

		}
		/** restore the attributes: **/

		/*
		 * for (Object key : attributes.keySet()) session.setAttribute(key,
		 * attributes.get(key));
		 */

		return new ResponseEntity<>(param, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> logOut() {
		param = new HashMap<String, String>();
		headers = Utilities.getDefaultHeader();
		try {
			logger.debug("you are logging out:");
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession();
			session.stop();
			SecurityUtils.getSubject().logout();
			headers.add(Constants.STATUS, HttpStatus.OK.toString());
			headers.add(Constants.MESSAGE, "Successfully sign out");
			param.put("Status", HttpStatus.OK.toString());
		} catch (InvalidSessionException e) {
			logger.debug("Error: While logging out :");

		}

		return new ResponseEntity<>(param, headers, HttpStatus.OK);
	}
}
