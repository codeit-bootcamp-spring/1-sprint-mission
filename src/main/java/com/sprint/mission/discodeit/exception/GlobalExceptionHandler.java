package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        ErrorResponse errorResponse = ErrorResponse.from(e);

        return ResponseEntity
            .status(e.getErrorCode().getHttpStatus())
            .body(errorResponse);
    }
}