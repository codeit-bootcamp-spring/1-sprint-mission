package com.sprint.mission.discodeit.exception;

import jakarta.persistence.EntityExistsException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

//TODO: 전역예외 설정할 것
@Slf4j
@RestControllerAdvice //전역에서 발생하는 예외를 처리한다
public class GlobalExceptionHandler {

  //DisCodeitException 클래스로 커스텀 예외를 핸들링할 수 있음. 
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getErrorCode().getStatus(),
        e.getClass().getSimpleName(),
        e.getErrorCode().name(),
        e.getErrorCode().getMessage(),
        e.getDetails()
    );
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
  }

  //NoSuchElementException 핸들링
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    log.error("NoSuchElementException: ", e);
    ErrorResponse errorResponse = new ErrorResponse(
        ErrorCode.ELEMENTS_NOT_FOUND.getStatus(),
        e.getClass().getSimpleName(),
        ErrorCode.ELEMENTS_NOT_FOUND.name(),
        e.getMessage()
    );
    return ResponseEntity.status(ErrorCode.ELEMENTS_NOT_FOUND.getStatus()).body(errorResponse);
  }

  //IllegalArgumentException 핸들링
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        ErrorCode.ILLEGAL_ARGUMENT.getStatus(),
        e.getClass().getSimpleName(),
        ErrorCode.ILLEGAL_ARGUMENT.name(),
        e.getMessage()
    );
    return ResponseEntity.status(ErrorCode.ILLEGAL_ARGUMENT.getStatus()).body(errorResponse);
  }

  //InternalServer에러
  @ExceptionHandler(InternalServerError.class)
  public ResponseEntity<ErrorResponse> handleInternalServerError(InternalServerError e) {
    ErrorResponse errorResponse = new ErrorResponse(
        ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
        e.getClass().getSimpleName(),
        ErrorCode.INTERNAL_SERVER_ERROR.name(),
        e.getMessage()
    );
    return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(errorResponse);
  }

  //EntityExistsException 핸들러
  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        ErrorCode.DUPLICATE_ENTITY.getStatus(),
        e.getClass().getSimpleName(),
        ErrorCode.DUPLICATE_ENTITY.name(),
        e.getMessage()
    );
    return ResponseEntity.status(ErrorCode.DUPLICATE_ENTITY.getStatus()).body(errorResponse);
  }
}

