package com.rvtech.prms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@EqualsAndHashCode(callSuper=false)
public class BadRequestException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	private List<FieldErrorDetails>  errors;

    public BadRequestException() {
        super("The request has invalid parameters");
    }
	
    public BadRequestException( List<FieldErrorDetails>  errors) {
        super(errors.toString());
        this.errors=errors;
    }

    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(Map<String,String> message) {
        super(message.toString());
    }
	public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}