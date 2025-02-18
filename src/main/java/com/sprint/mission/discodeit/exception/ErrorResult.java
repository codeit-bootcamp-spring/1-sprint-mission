package com.sprint.mission.discodeit.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Value
public class ErrorResult {
    Instant timestamp;
    int status;
    String message;
    String url;

    public ErrorResult(HttpStatus status, String message, String url) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.message = message;
        this.url = url;
    }
}
