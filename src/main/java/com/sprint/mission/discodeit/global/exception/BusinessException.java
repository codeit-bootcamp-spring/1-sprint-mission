package com.sprint.mission.discodeit.global.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

// 발생할 예외를 처리할 에러 클래스
@Getter
public class BusinessException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  public BusinessException(ErrorCode errorCode) {
    this(errorCode, null);
  }

  public BusinessException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage(), null, false, false);
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = details;
  }
}
