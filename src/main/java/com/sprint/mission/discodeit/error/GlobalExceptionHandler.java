package com.sprint.mission.discodeit.error;

import com.sprint.mission.discodeit.mapper.ErrorResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(ErrorResponseMapper.INSTANCE.fromException(e, e.getErrorCode()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponseMapper.INSTANCE.fromException(e, ErrorCode.DEFAULT_ERROR));
  }
}
