package com.spirnt.mission.discodeit.dto;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private Instant timestamp;
    private String code;
    private String message;
    private Map<String, Object> details;
    private String exceptionType; // 발생한 예외의 클래스 이름
    private int status;   // HTTP 상태 코드

    public ErrorResponse(
        String code, String message, Map<String, Object> details,
        String exceptionType, int status) {
        this.timestamp = Instant.now();
        this.code = code;
        this.message = message;
        this.details = details;
        this.exceptionType = exceptionType;
        this.status = status;
    }
}
