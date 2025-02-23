package com.sprint.mission.discodeit.global.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"timestamp", "status", "error", "message", "detail"})
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String detail;

    @Builder
    public ErrorResponse(ErrorCode errorCode, String detail) {
        this.timestamp = LocalDateTime.now();
        this.status = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
        this.detail = detail;
    }
}
