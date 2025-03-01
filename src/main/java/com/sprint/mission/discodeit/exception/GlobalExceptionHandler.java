package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                "404",
                ex.getMessage(),
                LocalDateTime.now()
        );
        // .status(HttpStatus.BAD_REQUEST) <- 에러코드 지정안할 시 200으로 넘어간다.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                "400",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }


    public static class ErrorResponse{
        private String errorCode;
        private String errorMessage;
        private LocalDateTime timestamp;

        public ErrorResponse(String errorCode, String errorMessage, LocalDateTime timestamp){
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.timestamp = timestamp;
        }
    }
}
