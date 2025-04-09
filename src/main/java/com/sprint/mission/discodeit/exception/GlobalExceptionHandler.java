package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.UserException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        log.error("DiscodeitException: {}", e.getMessage(), e);
        
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = getHttpStatus(errorCode);
        
        ErrorResponse response = ErrorResponse.of(
            errorCode.name(),
            e.getMessage(),
            e.getClass().getSimpleName(),
            status.value()
        );
        
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        log.error("UserException: {}", e.getMessage(), e);
        
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = getHttpStatus(errorCode);
        
        ErrorResponse response = ErrorResponse.of(
            errorCode.name(),
            e.getMessage(),
            e.getClass().getSimpleName(),
            status.value()
        );
        
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ChannelException.class)
    public ResponseEntity<ErrorResponse> handleChannelException(ChannelException e) {
        log.error("ChannelException: {}", e.getMessage(), e);
        
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = getHttpStatus(errorCode);
        
        ErrorResponse response = ErrorResponse.of(
            errorCode.name(),
            e.getMessage(),
            e.getClass().getSimpleName(),
            status.value()
        );
        
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorResponse> handleMessageException(MessageException e) {
        log.error("MessageException: {}", e.getMessage(), e);
        
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = getHttpStatus(errorCode);
        
        ErrorResponse response = ErrorResponse.of(
            errorCode.name(),
            e.getMessage(),
            e.getClass().getSimpleName(),
            status.value()
        );
        
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileException(FileException e) {
        log.error("FileException: {}", e.getMessage(), e);
        
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = getHttpStatus(errorCode);
        
        ErrorResponse response = ErrorResponse.of(
            errorCode.name(),
            e.getMessage(),
            e.getClass().getSimpleName(),
            status.value()
        );
        
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage(), e);
        
        Map<String, Object> details = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            details.put(error.getField(), error.getDefaultMessage());
        }
        
        ErrorResponse response = ErrorResponse.of(
            "INVALID_INPUT",
            "입력값이 유효하지 않습니다.",
            details,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint violation: {}", e.getMessage(), e);
        
        Map<String, Object> details = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> 
            details.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        
        ErrorResponse response = ErrorResponse.of(
            "CONSTRAINT_VIOLATION",
            "제약 조건을 위반했습니다.",
            details,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("File size exceeded: {}", e.getMessage(), e);
        
        ErrorResponse response = ErrorResponse.of(
            "FILE_SIZE_EXCEEDED",
            "파일 크기가 제한을 초과했습니다.",
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        
        ErrorResponse response = ErrorResponse.of(
            "INTERNAL_SERVER_ERROR",
            "서버 내부 오류가 발생했습니다.",
            e.getClass().getSimpleName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity.internalServerError().body(response);
    }

    private HttpStatus getHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND, FILE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER, DUPLICATE_EMAIL, DUPLICATE_USERNAME, INVALID_PASSWORD,
                PRIVATE_CHANNEL_UPDATE, CHANNEL_ACCESS_DENIED, MESSAGE_UPDATE_DENIED,
                MESSAGE_DELETE_DENIED, INVALID_FILE_TYPE, FILE_SIZE_EXCEEDED -> HttpStatus.BAD_REQUEST;
            case FILE_UPLOAD_FAILED, FILE_DOWNLOAD_FAILED, FILE_DELETE_FAILED -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
