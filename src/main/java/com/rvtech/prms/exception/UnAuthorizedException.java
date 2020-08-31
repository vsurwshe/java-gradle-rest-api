package com.rvtech.prms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

    public UnAuthorizedException() {
        super("You are Un Authorized");
    }
	
    public UnAuthorizedException(String message) {
        super(message);
    }
	
	public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}