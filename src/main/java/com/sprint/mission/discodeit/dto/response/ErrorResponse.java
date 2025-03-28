package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private Instant timestamp;
  private String message;
  private String code;
  private Map<String, Object> details;
  private String exceptionType; // 발생한 예외 클래스의 이름. 변수 명이 잘 드러나지 않음. exception Class Type?
  private int status; // 상태 코드

  public ErrorResponse(Instant timestamp, String message, String code, Map<String, Object> details,
      String exceptionType, int status) {
    this.timestamp = timestamp;
    this.message = message;
    this.code = code;
    this.details = details;
    this.exceptionType = exceptionType;
    this.status = status;
  }

  public ErrorResponse of(

  ) {
    return new ErrorResponse(
        Instant.now(),
        message,
        code,
        details,
        exceptionType,
        status
    );
  }
}
