package com.rvtech.prms.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rvtech.prms.common.SignInRequest;
import com.rvtech.prms.common.UserAccountDto;
import com.rvtech.prms.services.AccountServiceImpl;

@Component
public class RVRealm extends AuthorizingRealm {


	@Autowired
	private AccountServiceImpl accountService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		String loginType = token.getHost();
		SimpleAuthenticationInfo saInfo = null;
		UserAccountDto userAccountDto = this.accountService.getByUserNameOrEmailForLogin(token.getUsername());
		SignInRequest authDto = this.accountService.getAuth(userAccountDto.getAccountId(),
				userAccountDto.getUserName() == null ? userAccountDto.getEmailId() : userAccountDto.getUserName());
		saInfo = new SimpleAuthenticationInfo(userAccountDto, authDto.getPassword(), getName());
		return saInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
