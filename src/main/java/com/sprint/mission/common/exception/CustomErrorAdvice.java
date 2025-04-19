package com.sprint.mission.common.exception;

import com.sprint.mission.controller.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(basePackages = {"com.sprint.mission.controller"})
public class CustomErrorAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e, HttpServletRequest request) {
        return CustomErrorResponse.toResponseEntity(e.getErrorCode(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String validationMessage = fieldError == null
                ? "유효하지 않은 입력값입니다"
                : fieldError.getDefaultMessage();

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        CustomErrorResponse.builder()
                                .status(BAD_REQUEST.value()+"")
                                .message(validationMessage)
                                .errorCode(BAD_REQUEST.getReasonPhrase())
                                .build()
                );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception e, HttpServletRequest request) {

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        CustomErrorResponse.builder()
                                .status(INTERNAL_SERVER_ERROR.value()+"")
                                .message(e.getMessage())
                                .build()
                );
    }
}
