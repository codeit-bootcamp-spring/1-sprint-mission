package com.sprint.mission.common.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class CommonErrorResponse {

  private int status;
  private String message;
  private String errorCode;

  public static ResponseEntity<CommonErrorResponse> toResponseEntity(ErrorCode e) {
    return ResponseEntity
        .status(e.getStatus())
        .body(CommonErrorResponse.builder()
            .status(e.getStatus().value())
            .message(e.getMessage())
            .errorCode(e.getStatus().getReasonPhrase())
            .build());
  }
  // 결과 예시 : {"status":400,"message":"잘못된 요청입니다.","errorCode":"BAD_REQUEST"}

}
