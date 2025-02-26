package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 오류확인용
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e) {
        // 예외 메시지/스택을 로그에 남기기
        System.err.println("handleAll - caught exception: " + e.getMessage());
        e.printStackTrace(); // 혹은 log.error("Error!", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("글로벌 잘못된 요청: " + e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointer(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 오류(NullPointer): " + e.getMessage());
    }
}
