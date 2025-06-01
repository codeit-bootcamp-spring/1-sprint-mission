package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;   // 발생한 예외의 클래스 이름
  private final int status;    // HTTP 상태 코드

  public ErrorResponse(DiscodeitException exception) {
    this(
        Instant.now(),
        exception.getErrorCode().name(),
        exception.getMessage(),
        exception.getDetails(),
        exception.getClass().getSimpleName(),
        exception.getErrorCode().getStatus()
    );
  }

  public ErrorResponse(Exception exception, int status) {
    this(
        Instant.now(),
        exception.getClass().getSimpleName(),
        exception.getMessage(),
        new HashMap<>(),
        exception.getClass().getSimpleName(),
        status
    );
  }
}
