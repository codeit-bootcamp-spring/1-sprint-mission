package com.sprint.mission.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest request){
        return ErrorResponse.toResponseEntity(e.getErrorCode(), request.getRequestURI());
    }


//
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
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> exceptionHandler(Exception e){
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("code", "500");
//        map.put("error type", status.getReasonPhrase());
//
//        if (e.getMessage().isBlank()) map.put("message", "서버 내 에러 발생");
//        else map.put("message", e.toString() + e.getCause());
//        return new ResponseEntity<>(map, status);
//    }
}
