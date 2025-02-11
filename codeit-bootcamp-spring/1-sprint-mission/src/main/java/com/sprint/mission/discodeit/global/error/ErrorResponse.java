package com.sprint.mission.discodeit.global.error;

import com.sprint.mission.discodeit.global.error.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    private String description;
    private int status;
    private List<FieldError> fieldErrors;

    protected ErrorResponse() {
    }

    public ErrorResponse(String description, int status) {
        this.description = description;
        this.status = status;
        fieldErrors = new ArrayList<>();
    }

    public ErrorResponse(BusinessException businessException) {
        this.description = businessException.getErrorCode().getDescription();
        this.status = businessException.getErrorCode().getStatus();
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}
