package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  /**
   * 예외 발생 상황에 대한 추가정보를 저장하기 위한 속성 e.g. 조회 시도한 사용자의 ID 정보 e.g. 업데이트 시도한 PRIVATE 채널의 ID 정보
   **/
  private final Map<String, Object> details = new HashMap<>();
  ;

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
  }

  public DiscodeitException(ErrorCode errorCode, Instant timestamp) {
    super(errorCode.getMessage());
    this.timestamp = timestamp;
    this.errorCode = errorCode;
  }

  public DiscodeitException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    this(errorCode, timestamp);
    this.details.putAll(details);
  }

  public DiscodeitException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    this(errorCode);
    this.details.putAll(details);
  }
}
