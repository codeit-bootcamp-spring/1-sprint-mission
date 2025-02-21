package com.sprint.mission.common.exception;

import com.sprint.mission.common.ErrorCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private String path;
    private Instant time;
    //private String body;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode e, String path){

        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getStatus().value())  // 결과 예시: 400
                        .message(e.getMessage()) // 결과 예시: "Bad Request"
                        .path(path)
                        .time(Instant.now())
                        .build());
    }
}
