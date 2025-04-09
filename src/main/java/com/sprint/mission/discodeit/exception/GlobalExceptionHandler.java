package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 존재하지 않는 요청에 대한 예외
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public CustomApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("Invalid route or method: : {}", e.getMessage());
        DiscodeitException discodeitException = new DiscodeitException(ErrorCode.METHOD_NOT_ALLOWED);
        return CustomApiResponse.fail(ExceptionDto.of(discodeitException));
    }

    // Validation 예외
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public CustomApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> String.format("[field=%s, rejected=%s, message=%s]",
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .toList();

        log.warn("Validation Failed: {}", errors);
        DiscodeitException discodeitException = new DiscodeitException(ErrorCode.INVALID_REQUEST, Map.of("validationError", errors));
        return CustomApiResponse.fail(ExceptionDto.of(discodeitException));
    }

    // 커스텀 예외
    @ExceptionHandler(value = {DiscodeitException.class})
    public CustomApiResponse<?> handleCustomException(DiscodeitException e) {
        ExceptionDto exceptionDto = ExceptionDto.of(e);
        log.warn("DiscodeitException caught - exceptionType: {} | detail: {}", exceptionDto.getExceptionType(), exceptionDto);
        return CustomApiResponse.fail(exceptionDto);
    }

    // 기본 예외
    @ExceptionHandler(value = {Exception.class})
    public CustomApiResponse<?> handleException(Exception e) {
        log.error("Unhandled exception caught in GlobalExceptionHandler : {}", e.getMessage());
        DiscodeitException discodeitException = new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR);
        return CustomApiResponse.fail(ExceptionDto.of(discodeitException));
    }
}
