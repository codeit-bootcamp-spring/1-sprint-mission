package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomRuntimeException(ErrorCode errorCode) {
      super("[" + errorCode.toString() + "] " + ": " + errorCode.getDescription());
      this.errorCode = errorCode;
    }

}
