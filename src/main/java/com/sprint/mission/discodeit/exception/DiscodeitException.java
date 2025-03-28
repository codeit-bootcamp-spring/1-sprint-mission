package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;  // TODO 꼭 Map 으로 해야하나? 이렇게 한 이유는 무엇이고 다르게 할 수 있는 방법은 ?

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = details;
  }
}
