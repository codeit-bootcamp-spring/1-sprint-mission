package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

  private final Instant timestamp;
  private final String message;
  private final String code;
  @Builder.Default
  private Map<String, Object> details = new HashMap<>();
  private final String exceptionType;
  private final int status;

  @Builder
  private ErrorResponse(
      Instant timestamp,
      String message,
      String code,
      Map<String, Object> details,
      String exceptionType,
      int status
  ) {
    this.timestamp = timestamp;
    this.message = message;
    this.code = code;
    this.details = details;
    this.exceptionType = exceptionType;
    this.status = status;
  }

  public static ErrorResponse of(
      DiscodeitException discodeitException
  ) {
    ErrorCode errorCode = discodeitException.getErrorCode();

    return ErrorResponse.builder()
        .timestamp(discodeitException.getTimestamp())
        .message(errorCode.getMessage())
        .code(errorCode.getCode())
        .details(discodeitException.getDetails())
        .exceptionType(discodeitException.getExceptionTypeName())
        .status(errorCode.getStatus().value())
        .build();
  }


  public static ErrorResponse ofUnknown(
      Exception exception
  ) {
    ErrorCode errorCoder = ErrorCode.UNKNOWN;

    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .message(exception.getMessage())
        .code(errorCoder.getCode())
        .exceptionType(Exception.class.getSimpleName())
        .status(errorCoder.getStatus().value())
        .build();
  }
}
