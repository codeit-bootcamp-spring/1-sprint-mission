package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.binary.BinaryContentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BinaryControllerExceptionAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(BinaryContentNotFoundException.class)
  public ErrorResponse notFoundFile(BinaryContentNotFoundException e) {
    return new ErrorResponse(e.getMessage());
  }
}
