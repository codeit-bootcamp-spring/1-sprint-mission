package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //전역에서 발생하는 예외를 처리한다
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> loginFailedException(LoginFailedException e) {
        return new ResponseEntity<>("로그인에 실패하였습니다.", HttpStatus.UNAUTHORIZED);
    }
}
