package com.sprint.mission.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class CommonResponse<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;

    @Schema(description = "응답 메시지", example = "성공")
    private String message;

    @Schema(description = "응답 데이터")
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
