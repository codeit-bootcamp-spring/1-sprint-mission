package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.exception.security.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Controller + ResponseBody
@Slf4j
@RestControllerAdvice(basePackages = {"com.sprint.mission.discodeit.controller.api"})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 인증 실패 > 사용자 이름 혹은 비밀번호가 잘못된 경우
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(ErrorCode.AUTH_UNAUTHORIZED.getHttpStatus().value())
            .exceptionType(ex.getClass().getSimpleName())
            .code(ErrorCode.AUTH_UNAUTHORIZED.name())
            .message(ex.getMessage())
            .build();

        return handleExceptionInternal(errorResponse);
    }

    // 토큰 관련 오류 > JWT 만료, 형식 오류 등
    @ExceptionHandler({ExpiredJwtException.class, InvalidTokenException.class})
    public ResponseEntity<ErrorResponse> handleTokenException(Exception ex) {
        String code = ex instanceof ExpiredJwtException ? "TOKEN_EXPIRED" : "INVALID_TOKEN";

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(ErrorCode.AUTH_UNAUTHORIZED.getHttpStatus().value())
            .exceptionType(ex.getClass().getSimpleName())
            .code(ErrorCode.AUTH_UNAUTHORIZED.name())
            .message("토큰이 유효하지 않습니다. 다시 로그인 해 주세요.")
            .build();

        return handleExceptionInternal(errorResponse);
    }

    // 유효성 검사에 대한 예외 처리하기
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        FieldError fieldError = ex.getFieldErrors().get(0);

        log.info("{} - {}", errorCode.name(),
            Map.of(fieldError.getField(), fieldError.getRejectedValue()));

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(Instant.now())
            .code(errorCode.name())
            .message(fieldError.getDefaultMessage())
            .details(Map.of(fieldError.getField(), fieldError.getRejectedValue()))
            .exceptionType(ex.getClass().getSimpleName())
            .status(errorCode.getHttpStatus().value())
            .build();

        return ResponseEntity
            .status(errorResponse.getStatus())
            .body(errorResponse);
    }

    // BusinessException 에 대한 예외 처리하기
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
        BusinessException ex) {

        ErrorCode errorCode = ex.getErrorCode();
        log.info("{} - {}: {}", ex.getClass().getSimpleName(), errorCode.getMessage(),
            ex.getDetails());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(ex.getTimestamp())
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .details(ex.getDetails())
            .exceptionType(ex.getClass().getSimpleName())
            .status(errorCode.getHttpStatus().value())
            .build();

        return handleExceptionInternal(errorResponse);
    }

    // 정의된 예외 이외에 모든 예외처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleAllException(Exception ex) {
        // TODO : StackTrace 관련 설정 추가
        log.error("Unexpected error occurred", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(Instant.now())
            .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
            .message(ex.getMessage())
            .exceptionType(ex.getClass().getSimpleName())
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
            .build();

        return handleExceptionInternal(errorResponse);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorResponse errorResponse) {
        return ResponseEntity
            .status(errorResponse.getStatus())
            .body(errorResponse);
    }

}
