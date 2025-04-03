package com.sprint.mission.discodeit.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class ErrorResponse {
    Instant timestamp;
    String code;
    String message;
    Map<String, Object> details;
    String exceptionType;
    int status;

    @Builder
    public ErrorResponse(String code, String message, Map<String, Object> details, String exceptionType, int status) {
        this.timestamp = Instant.now();
        this.message = message;
        this.details = details;
        this.exceptionType = exceptionType;
        this.status = status; // HTTP 상태코드
    }
}
