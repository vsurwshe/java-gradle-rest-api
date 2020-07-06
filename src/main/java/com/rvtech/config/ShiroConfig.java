package com.rvtech.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rvtech.realm.RVRealm;

@Configuration
public class ShiroConfig {

	/*
	 * <bean id="phrRealm" class="com.health5c.ccp.security.realm.shiro.PHRRealm">
	 * name="authorizationCachingEnabled" value="true" /> <property
	 * name="authenticationCacheName"
	 * value="com.health5c.ccp.security.realm.shiro.PHRRealm.authenticationCache" />
	 * <property name="authorizationCacheName"
	 * value="com.health5c.ccp.security.realm.shiro.PHRRealm.authorizationCache" />
	 * <!-- <property name="credentialsMatcher" ref="credentialsMatcher" /> -->
	 * <property name="credentialsMatcher" ref="passwordMatcher" /> <property
	 * name="cacheManager" ref="cacheManager" /> </bean>
	 */

	@Bean(name = "randomNumberGenerator")
	public SecureRandomNumberGenerator randomNumberGenerator() {
		SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
		return randomNumberGenerator;
	}

	@Bean(name = "hashService")
	public DefaultHashService hashService() {
		String byteSource = "$3!DiOTs$";
		DefaultHashService hashService = new DefaultHashService();
		hashService.setHashAlgorithmName("SHA-512");
		hashService.setHashIterations(500000);
		hashService.setPrivateSalt(ByteSource.Util.bytes(byteSource));
		hashService.setGeneratePublicSalt(false);
		hashService.setRandomNumberGenerator(randomNumberGenerator());
		return hashService;
	}

	@Bean(name = "passwordService")
	public DefaultPasswordService passwordService() {
		DefaultPasswordService passwordService = new DefaultPasswordService();
		passwordService.setHashService(hashService());
		return passwordService;
	}

	@Bean(name = "passwordMatcher")
	public PasswordMatcher passwordMatcher() {
		PasswordMatcher passwordMatcher = new PasswordMatcher();
		passwordMatcher.setPasswordService(passwordService());
		return passwordMatcher;
	}

	@Bean(name = "rvRealm")
	public RVRealm rvRealm() {
		RVRealm rvRealm = new RVRealm();
		rvRealm.setName("rvRealm");
		rvRealm.setAuthenticationCachingEnabled(true);
		rvRealm.setAuthorizationCachingEnabled(true);
		rvRealm.setCredentialsMatcher(passwordMatcher());
		return rvRealm;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager defaultWebSecurityManager() {
		System.out.println("DefaultWebSecurityManager");
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(rvRealm());
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	@Bean(name = "sessionManager")
	public SessionManager sessionManager() {
		System.out.println("SessionManager");
		DefaultSessionManager sessionManager = new DefaultSessionManager();
		sessionManager.setGlobalSessionTimeout(3600000);
		return sessionManager;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
		System.out.println("ShiroFilterFactoryBean");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
		filterChainDefinitionMap.put("/authentication/signIn/**", "anon");
		filterChainDefinitionMap.put("/swagger-ui.html", "authc");

		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		Map<String, Filter> filterMap = new HashMap<String, Filter>();
		filterMap.put("anon",  new CorsFilter());
		filterMap.put("authc", new RestPassThruAuthTokenFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
		return shiroFilterFactoryBean;
	}

}