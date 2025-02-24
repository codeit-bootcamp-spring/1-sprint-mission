package com.sprint.mission.common.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@Hidden
@RestControllerAdvice(basePackages = {"com.sprint.mission.controller"})
public class CustomErrorAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e, HttpServletRequest request) {
        return CustomErrorResponse.toResponseEntity(e.getErrorCode(), request);
    }

    //    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
//    public ResponseEntity<Map<String, String>> argsMissMatchHandler(MethodArgumentTypeMismatchException e){
//
//        HashMap<String, String> map = new HashMap<>();
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        map.put("code", "400");
//        map.put("error type", status.getReasonPhrase());
//        if (e.getMessage().isBlank()) map.put("message", "Invalid type or value");
//        else map.put("message", e.getMessage());
//        return new ResponseEntity<>(map, status);
//    }
//
//
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        CustomErrorResponse.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message(e.getMessage())
                                .build()
                );
        //.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
