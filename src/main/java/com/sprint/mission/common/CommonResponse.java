package com.sprint.mission.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class CommonResponse<T> {

    private int status;
    private String message;
    private T data;

    public static ResponseEntity<CommonResponse> toResponseEntity(HttpStatus status, String message, Object data){
        if (data == null)  data = "";
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .build());
    }

}
