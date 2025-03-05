package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.readstatus.ReadStatusExistsException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReadStatusExceptionAdvice {

  /**
   * 이미 존재함
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ReadStatusExistsException.class)
  public ErrorResponse existsReadStatus(ReadStatusExistsException e) {
    return new ErrorResponse(e.getMessage());
  }

  /**
   * 읽은 상태 찾지 못함
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ErrorResponse notFoundReadStatus(ReadStatusNotFoundException e) {
    return new ErrorResponse(e.getMessage());
  }
}
