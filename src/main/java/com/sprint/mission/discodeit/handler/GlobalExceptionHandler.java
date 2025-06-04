package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {

    ErrorResponse er = new ErrorResponse(
        ex.getTimestamp(),
        ex.getErrorCode().getCode(),
        ex.getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(),
        ex.getStatusCode()
    );

    return ResponseEntity.status(ex.getStatusCode()).body(er);
  }

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
    ErrorResponse er = new ErrorResponse(
        ex.getTimestamp(),
        ex.getErrorCode().getCode(),
        ex.getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(),
        ex.getStatusCode()
    );

    return ResponseEntity.status(401).body(er);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        errors,
        ex.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );

    return ResponseEntity.status(er.getStatus()).body(er);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, Object> errors = ex.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> violation.getPropertyPath().toString(),
            violation -> violation.getMessage(),
            (existing, replacement) -> existing
        ));

    ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        errors,
        ex.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );

    return ResponseEntity.status(er.getStatus()).body(er);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentMismatchException(
      MethodArgumentTypeMismatchException ex) {
    Map<String, Object> details = new HashMap<>();

    details.put(ex.getParameter().getParameterName(), ex.getValue());
    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        "INVALID FORMAT",
        "잘못된 형식의 입력입니다.",
        details,
        ex.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );

    return ResponseEntity.status(er.getStatus()).body(er);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    Map<String, Object> details = new HashMap<>();
    details.put("error", ex.getMostSpecificCause().getMessage());

    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        "DUPLICATE_ERROR",
        "중복된 데이터로 인한 오류가 발생했습니다.",
        details,
        ex.getClass().getSimpleName(),
        HttpStatus.CONFLICT.value()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {

    log.warn("[UNHANDLED ERROR]", ex);
    ErrorCode errorCode = ErrorCode.UNHANDLED_ERROR;

    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        Map.of("cause", "Uncaught Error"),
        ex.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );

    return ResponseEntity.status(er.getStatus()).body(er);
  }
}
