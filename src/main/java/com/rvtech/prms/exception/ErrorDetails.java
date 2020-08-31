package com.rvtech.prms.exception;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    private List<FieldErrorDetails>  errors;
    private Date timestamp;
    private int status;
    private String path;

    public ErrorDetails(Date timestamp,List<FieldErrorDetails> errors,int status, String path) {
        super();
        this.timestamp = timestamp;
        this.errors = errors;
        this.status=status;
        this.path=path;
    }
}
