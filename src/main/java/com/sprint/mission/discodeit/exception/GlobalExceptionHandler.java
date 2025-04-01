package com.sprint.mission.discodeit.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e, HttpServletRequest request) {
        HttpStatus status = mapToHttpStatus(e.getErrorCode());
        log.warn("DiscodeitException at [{}]: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(status).body(ErrorResponse.of(e, status.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception at [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                "INTERNAL_SERVER_ERROR",
                "예기치 못한 서버 오류가 발생했습니다.",
                null,
                e.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private HttpStatus mapToHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND ,MESSAGE_NOT_FOUND,
                 READ_STATUS_NOT_FOUND, USER_STATUS_NOT_FOUND, INVALID_FILE -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER, READ_STATUS_ALREADY_EXISTS, USER_STATUS_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case PRIVATE_CHANNEL_UPDATE, UNAUTHORIZED -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
