package com.sprint.mission.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class CommonResponse<T> {

    @Schema(description = "HTTP 상태 코드", example = "2xx")
    private String status;

    @Schema(description = "응답 메시지", example = "성공")
    private String message;

    @Schema(description = "응답 데이터", example = "{status:200, message:성공, data:{객체}}")
    private T data;

    public static ResponseEntity<CommonResponse> toResponseEntity(HttpStatus status, String message, Object data){
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .status(status.value()+"")
                        .message(message)
                        .data(data)
                        .build());
    }

    // API(매개변수, 반환값)에 null을 최대한 쓰지 말기 원칙 中 null 잘 다루기
    public static ResponseEntity<CommonResponse> toResponseEntityWithoutData(HttpStatus status, String message){
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .status(status.value()+"")
                        .message(message)
                        .data(null)
                        .build());
    }
}
