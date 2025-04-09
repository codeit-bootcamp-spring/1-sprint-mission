package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class DiscodeitException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Instant timestamp;
    private final Map<String, Object> details = new HashMap<>();

    public DiscodeitException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.details.putAll(details);
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
