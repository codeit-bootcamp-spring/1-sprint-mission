package com.sprint.mission.discodeit.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DiscodeitException extends RuntimeException{
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = new HashMap<>();
    }
}
