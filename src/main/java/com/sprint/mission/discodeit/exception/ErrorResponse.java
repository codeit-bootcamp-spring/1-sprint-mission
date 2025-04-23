package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final Instant timestamp;
    private final String code;
    private final String message;
    private final Map<String, Object> details;
    private final String exceptionType;
    private final int status;

    public ErrorResponse(DiscodeitException exception, int status) {
        this(Instant.now(), exception.getErrorCode().name(), exception.getMessage(), exception.getDetails(), exception.getClass().getSimpleName(), status);
    }

    public ErrorResponse(Exception exception, int status) {
        this(Instant.now(), exception.getClass().getSimpleName(), exception.getMessage(), new HashMap<>(), exception.getClass().getSimpleName(), status);
    }
} 