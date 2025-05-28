package com.spirnt.mission.discodeit.exception;

import com.spirnt.mission.discodeit.dto.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DiscodeitExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(
                new ErrorResponse(errorCode.getCode(),
                    errorCode.getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), errorCode.getStatus().value()));
    }

}