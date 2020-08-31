package com.rvtech.prms.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldErrorDetails {

    private String fieldName;
    private String defaultMessage;

    public FieldErrorDetails(String fieldName, String defaultMessage) {
        super();
        this.fieldName = fieldName;
        this.defaultMessage = defaultMessage;
    }
}
