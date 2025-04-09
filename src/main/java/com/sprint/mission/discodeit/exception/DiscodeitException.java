package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  //에러객체 생성 시간 초기화
  final Instant timestamp = Instant.now();
  final ErrorCode errorCode;
  final Map<String, Object> details;

  protected DiscodeitException(ErrorCode errorCode, String message, Map<String, Object> details) {
    super(message);
    this.errorCode = errorCode;
    this.details = details;
  }

  protected DiscodeitException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  protected DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    this(errorCode, errorCode.getMessage(), details);
  }

  protected DiscodeitException(ErrorCode errorCode) {
    this(errorCode, errorCode.getMessage(), null);
  }
}
