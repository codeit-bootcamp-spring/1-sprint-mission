package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class RestApiException extends RuntimeException {

  private final DomainErrorCode errorCode;
  private final String details;
  
  public RestApiException(DomainErrorCode errorCode, String details) {
    this.errorCode = errorCode;
    this.details = details;
  }
  
  public RestApiException(DomainErrorCode errorCode) {
    this.errorCode = errorCode;
    this.details = errorCode.getMessage();
  }
}
