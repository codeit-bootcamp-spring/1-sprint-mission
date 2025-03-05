package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getErrorCode().getCode(),
        e.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(FileUploadException.class)
  public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "The request was rejected because no multipart boundary was found",
        LocalDateTime.now()
    );
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(InvalidContentTypeException.class)
  public ResponseEntity<ErrorResponse> handleInvalidContentTypeException(
      InvalidContentTypeException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        e.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class ErrorResponse {

    private int errorCode;
    private String errorMessage;
    private LocalDateTime timestamp;
  }
}
