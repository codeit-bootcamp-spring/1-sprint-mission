package com.sprint.mission.controller.advice;

import com.sprint.mission.service.exception.DuplicateName;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = NotFoundId.class)
    public ResponseEntity<Map<String, String>> notFoundExHandler(NotFoundId e){
        HttpStatus status = HttpStatus.NOT_FOUND;

        Map<String, String> map = new HashMap<>();
        map.put("code", "404");
        map.put("error type",status.getReasonPhrase());
        if (e.getMessage().isBlank()) map.put("message", "Not Found ID");
        else map.put("message", e.getMessage());

        return new ResponseEntity<>(map, status);
    }

    @ExceptionHandler(DuplicateName.class)
    public ResponseEntity<Map<String, String>> duplicateExHandler(DuplicateName e){

        HttpStatus status = HttpStatus.CONFLICT;

        HashMap<String, String> map = new HashMap<>();
        map.put("code", "409");
        map.put("error type", status.getReasonPhrase());
        if (e.getMessage().isBlank()) map.put("message", "Duplicate");
        else map.put("message", e.getMessage());

        return new ResponseEntity<>(map, status);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(Exception e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        HashMap<String, String> map = new HashMap<>();
        map.put("code", "500");
        map.put("error type", status.getReasonPhrase());
        if (e.getMessage().isBlank()) map.put("message", "서버 내 에러 발생");
        else map.put("message", e.getMessage());
        return new ResponseEntity<>(map, status);
    }
}
