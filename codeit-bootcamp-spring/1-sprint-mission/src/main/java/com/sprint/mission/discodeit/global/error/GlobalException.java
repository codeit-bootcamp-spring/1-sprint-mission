package com.sprint.mission.discodeit.global.error;

import com.sprint.mission.discodeit.global.error.exception.EntityNotFoundException;
import com.sprint.mission.discodeit.global.error.exception.InvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidException(InvalidException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode().getDescription(),
                exception.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode().getDescription(),
                exception.getErrorCode().getStatus());
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
