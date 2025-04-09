package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

  private Instant timestamp;
  private String code;
  private String message;
  private Map<String, Object> details;
  private String exceptionType;
  private int status;

  public static ErrorResponseBuilder builder() {
    return new ErrorResponseBuilder()
        .timestamp(Instant.now())
        .details(new HashMap<>());
  }

  public static ErrorResponse of(DiscodeitException e) {
    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().name())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(e.getErrorCode().getHttpStatus().value())
        .build();
  }
}