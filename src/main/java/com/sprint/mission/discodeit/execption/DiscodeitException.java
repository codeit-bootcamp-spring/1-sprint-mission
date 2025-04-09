package com.sprint.mission.discodeit.execption;

import com.sprint.mission.discodeit.common.util.TimeUtil;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private Map<String, Object> details = new HashMap<>();

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.timestamp = TimeUtil.getCurrentTime();
    this.errorCode = errorCode;
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.timestamp = TimeUtil.getCurrentTime();
    this.errorCode = errorCode;
    this.details = details;
  }
}
