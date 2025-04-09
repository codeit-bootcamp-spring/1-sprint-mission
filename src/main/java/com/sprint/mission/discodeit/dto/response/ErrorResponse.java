package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.Map;
import lombok.Builder;

@Builder
public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {
    public static ErrorResponse of(String code, String message, String exceptionType, int status) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .code(code)
            .message(message)
            .exceptionType(exceptionType)
            .status(status)
            .build();
    }

    public static ErrorResponse of(String code, String message, Map<String, Object> details, 
        String exceptionType, int status) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .code(code)
            .message(message)
            .details(details)
            .exceptionType(exceptionType)
            .status(status)
            .build();
    }
} 