package com.sprint.mission.discodeit.exception;


import com.sprint.mission.discodeit.dto.error.ErrorResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorCode code = e.getErrorCode();
    HttpStatus status = HttpStatus.valueOf(code.getStatus());

    ErrorResponse response = new ErrorResponse(
        e.getTimestamp(),
        code.getCode(),
        code.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        code.getStatus()
    );

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> habdleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, Object> details = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(err -> {
      String fieldName = ((FieldError) err).getField();
      String errorMessage = err.getDefaultMessage();
      details.put(fieldName, errorMessage);
      sb.append(fieldName + ": " + errorMessage).append("; ");
    });

    ErrorResponse body = new ErrorResponse(
        Instant.now(),
        "VALIDATION_FAILED",
        sb.toString(),
        details,
        ex.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.badRequest().body(body);
  }
}
