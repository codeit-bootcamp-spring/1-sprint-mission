package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResult {

  private String errorCode;
  private String message;

  public ErrorResult(ErrorCode errorCode) {
    this.errorCode = errorCode.getCode();
    this.message = errorCode.getMessage();
  }

  public ErrorResult(String message) {
    this.message = message;
  }
}
