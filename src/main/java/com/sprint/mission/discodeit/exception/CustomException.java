package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomException extends RuntimeException{
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode){
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public int getStatusCode(){
    return errorCode.getStatus().value();
  }
}
