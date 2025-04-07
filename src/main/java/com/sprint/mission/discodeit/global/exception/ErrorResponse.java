package com.sprint.mission.discodeit.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"timestamp", "exceptionType", "message", "code", "status", "details"})
public class ErrorResponse {

  private Instant timestamp;
  private String code;
  private String message;
  private Map<String, Object> details;
  private String exceptionType;
  private int status;

  @Builder
  public ErrorResponse(Instant timestamp, String code, String message, Map<String, Object> details,
      String exceptionType, int status) {
    this.timestamp = timestamp;
    this.code = code;
    this.message = message;
    this.details = details;
    this.exceptionType = exceptionType;
    this.status = status;
  }
}
