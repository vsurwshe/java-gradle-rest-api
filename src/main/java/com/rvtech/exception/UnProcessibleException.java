package com.rvtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnProcessibleException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

    public UnProcessibleException() {
        super("unable to process contained instructions");
    }
	
    public UnProcessibleException(String message) {
        super(message);
    }
	
	public UnProcessibleException(String message, Throwable cause) {
        super(message, cause);
    }

}