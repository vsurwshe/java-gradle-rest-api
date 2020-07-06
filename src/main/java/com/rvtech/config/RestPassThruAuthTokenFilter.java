package com.rvtech.config;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RestPassThruAuthTokenFilter extends PassThruAuthenticationFilter {

	/**
	 * Convenience method that acquires the Subject associated with the request.
	 * <p/>
	 * The default implementation simply returns
	 * {@link org.apache.shiro.SecurityUtils#getSubject()
	 * SecurityUtils.getSubject()}.
	 *
	 * @param request  the incoming <code>ServletRequest</code>
	 * @param response the outgoing <code>ServletResponse</code>
	 * @return the Subject associated with the request.
	 */
	protected Subject getSubject(ServletRequest request, ServletResponse response) {
		System.out.println("In Fillter ");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String sessionId = httpRequest.getHeader("Authorization");
		Map<String, String[]> mapData = request.getParameterMap();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization");
		Subject subject = new Subject.Builder().sessionId(sessionId).buildSubject();
		ThreadContext.bind(subject);
		return subject;
	}

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
		if (httpRequest.getMethod().equals("OPTIONS")) {
			return true;
		}
		if (isLoginRequest(request, response)) {
			return true;
		} else {
			WebUtils.toHttp(response).setStatus(HttpStatus.FORBIDDEN.value());
			WebUtils.toHttp(response).getWriter()
					.write("{ \"error\": \"User is not authenticated or the session has expired\" }");
			return false;
		}

	}

}