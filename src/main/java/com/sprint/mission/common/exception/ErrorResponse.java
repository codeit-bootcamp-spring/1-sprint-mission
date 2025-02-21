package com.sprint.mission.common.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private String path;
    private Instant time;
    //private String body;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode e, String path) {

        return ResponseEntity
                .status(e.getStatus().value())
                .body(ErrorResponse.builder()
                        .status(e.getStatus().value()) // 결과 예시 :
                        .message(e.getMessage())
                        .path(path)
                        .time(Instant.now())
                        .build());
    }
}
