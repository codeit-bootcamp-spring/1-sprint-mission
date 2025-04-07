package com.sprint.mission.discodeit.error;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DiscodeitException extends RuntimeException {
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode) {
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = new HashMap<>();
    }

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }
}
