package com.sprint.mission.common.exception;

import com.sprint.mission.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonErrorResponse> handleCustomException(CustomException e, HttpServletRequest request){
        return CommonErrorResponse.toResponseEntity(e.getErrorCode());
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
    public ResponseEntity<CommonErrorResponse> exceptionHandler(Exception e){
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                CommonErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build()
            );
            //.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
