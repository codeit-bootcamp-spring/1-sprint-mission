package com.sprint.mission.discodeit.exception;

public class CustomRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomRuntimeException(ErrorCode errorCode) {
      super("[" + errorCode.toString() + "] " + ": " + errorCode.getDescription());
      this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
      return errorCode;
    }
}
