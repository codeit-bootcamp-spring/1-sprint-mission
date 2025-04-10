package com.sprint.mission.discodeit.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e){
        log.warn("[{}] {} - {}", e.getErrorCode().name(), e.getMessage(), e.getDetails());
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(ErrorResponse.of(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e){
        // log.error("Fallback handler: {}", e.getClass().getName()); // 여기서 진짜 예외 타입 확인!

        log.error("Unhandled exception : {}", e.getMessage());
        DiscodeitException wrapped = new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR) {};

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of(wrapped));
    }

    // JSON 형식 유효성 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> details = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (m1, m2) -> m1));

        DiscodeitException wrapped = new DiscodeitException(ErrorCode.VALIDATION_ERROR) {};

        return ResponseEntity
            .status(ErrorCode.VALIDATION_ERROR.getStatus())
            .body(ErrorResponse.of(wrapped));
    }

    // multipart/form-data, form 기반 유효성 실패
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBind(BindException ex) {
        Map<String, Object> details = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (m1, m2) -> m1));

        DiscodeitException wrapped = new DiscodeitException(ErrorCode.VALIDATION_ERROR) {};


        return ResponseEntity
            .status(ErrorCode.VALIDATION_ERROR.getStatus())
            .body(ErrorResponse.of(wrapped));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        Map<String, String> details = ex.getAllValidationResults().stream()
            .flatMap(result -> result.getResolvableErrors().stream())
            .filter(error -> error instanceof FieldError)
            .map(error -> (FieldError) error)
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (msg1, msg2) -> msg1 // 중복 필드 처리
            ));

        log.warn("HandlerMethodValidationException 발생: {}", details);

        DiscodeitException wrapped = new DiscodeitException(ErrorCode.VALIDATION_ERROR) {};


        return ResponseEntity
            .status(ErrorCode.VALIDATION_ERROR.getStatus())
            .body(ErrorResponse.of(wrapped));
    }
}
