package com.rvtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class NoDataFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public NoDataFoundException() {
        super("No Records Found");
    }

    public NoDataFoundException(String message) {
        super(message);
    }

	public NoDataFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}