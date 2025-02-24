package com.sprint.mission.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@Schema(description = "커스텀 에러 응답")
public class CustomErrorResponse {

  @Schema(description = "HTTP 상태 코드", example = "4XX, 5XX")
  private String status;

  @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
  private String message;

  @Schema(description = "HTTP 에러 코드", example = "BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR etc..")
  private String errorCode;

  //  private String path;
//  private String timestamp;

  public static ResponseEntity<CustomErrorResponse> toResponseEntity(ErrorCode e) {
    HttpStatus eStatus = e.getStatus();
    return ResponseEntity
        .status(eStatus)
        .body(CustomErrorResponse.builder()
            .status(eStatus.value()+"")
            .message(e.getMessage())
            .errorCode(eStatus.getReasonPhrase())
            .build());
  }
  // 결과 예시 : {"status":400,"message":"잘못된 요청입니다.","errorCode":"BAD_REQUEST"}

  public static ResponseEntity<CustomErrorResponse> toResponseEntity(ErrorCode e, HttpServletRequest request) {
    HttpStatus eStatus = e.getStatus();
    return ResponseEntity
            .status(eStatus)
            .body(CustomErrorResponse.builder()
                    .status(eStatus.value()+"")
                    .message(e.getMessage())
                    .errorCode(eStatus.getReasonPhrase())
//                    .path(request.getRequestURI())
                    .build());
  }

}
