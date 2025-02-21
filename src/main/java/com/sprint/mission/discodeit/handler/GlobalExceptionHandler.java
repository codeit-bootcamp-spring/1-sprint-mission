package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.error.ErrorDetail;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    ErrorResponse er = new ErrorResponse(new ErrorDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex){
    ErrorResponse er = new ErrorResponse(new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
  }
  @ExceptionHandler(InvalidOperationException.class)
  public ResponseEntity<ErrorResponse> handleInvalidOperationException(InvalidOperationException ex) {
    ErrorResponse er = new ErrorResponse(new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
  }

}
