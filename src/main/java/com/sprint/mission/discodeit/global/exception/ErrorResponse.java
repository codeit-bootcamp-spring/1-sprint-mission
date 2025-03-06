package com.sprint.mission.discodeit.global.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"code", "name", "message", "detail"})
public class ErrorResponse {

  @JsonIgnore
  private final HttpStatus httpStatus;
  private final String code;
  private final String name;
  private final String message;
  private final String detail;

  @Builder
  public ErrorResponse(ErrorCode errorCode, String detail) {
    this.httpStatus = errorCode.getHttpStatus();
    this.name = errorCode.name();
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
    this.detail = detail;
  }
}
