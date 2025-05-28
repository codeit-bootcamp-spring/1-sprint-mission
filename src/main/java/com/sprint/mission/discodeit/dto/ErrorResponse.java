package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private Instant timestamp;
  private String code;
  private String message;
  private Map<String, Object> details;
  private String exceptionType;
  private int status;

  public static ErrorResponse from(DiscodeitException exception) {
    return ErrorResponse.builder()
        .timestamp(exception.getTimestamp())
        .code(exception.getErrorCode().name()) //ex)USER_NOT_FOUND
        .message(exception.getErrorCode().getMessage()) //"user을 찾을 수 없습니다"
        .details(exception.getDetails())
        .exceptionType(exception.getClass().getSimpleName())
        .status(exception.getErrorCode().getHttpStatus().value())
        .build();
  }

  public static ErrorResponse of(ErrorCode errorCode, Map<String, Object> details,
      Class<?> exceptionClass) {
    return new ErrorResponse(
        Instant.now(),
        errorCode.name(),
        errorCode.getMessage(),
        details,
        exceptionClass.getSimpleName(),
        errorCode.getHttpStatus().value()
    );
  }

  public static ErrorResponse from(Exception exception, int status) {
    return ErrorResponse.builder()
            .timestamp(Instant.now())
            .code(exception.getClass().getSimpleName())
            .message(exception.getMessage())
            .details(new HashMap<>())
            .exceptionType(exception.getClass().getSimpleName())
            .status(status)
            .build();
  }

}
