package com.sprint.mission.discodeit.execption;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Instant timestamp;
    private String errorCode;
    private String errorMessage;
    private Map<String, Object> details = new HashMap<>();
    private String exceptionType;
    private int status;

    public ErrorResponse(Exception exception, int status) {
        this.timestamp = Instant.now();
        this.errorCode = ErrorCode.UNAUTHORIZED.getCode();
        this.errorMessage = ErrorCode.UNAUTHORIZED.getMessage();
        this.exceptionType = exception.getClass().getSimpleName();
        this.status = status;
    }

    public ErrorResponse(String code, String message, String exception,
        int status) {
        this.timestamp = Instant.now();
        this.errorCode = code;
        this.errorMessage = message;
        this.exceptionType = exception;
        this.status = status;
    }

    public static ErrorResponse of(int status, String code, String message) {
        return new ErrorResponse(
            code,
            message,
            "JwtException",
            status
        );
    }

}
