package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//  오류확인용
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<String> handleAll(Exception e) {
//   예외 메시지/스택을 로그에 남기기
//    log.error("handleAll - caught exception: " + e.getMessage());
//    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
//  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
    log.error("handleIllegalArgument - caught exception: " + e.getMessage());
    return ResponseEntity.badRequest().body("글로벌 잘못된 요청: " + e.getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<String> handleNullPointer(NullPointerException e) {
    log.error("handleNullPointer - caught exception: " + e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("서버 내부 오류(NullPointer): " + e.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException e) {
    log.error("handleUserAlreadyExists - caught exception: " + e.getMessage());
    // 문법적으로는 올바르지만 리소스 현재 상태와 충돌 일 때
    return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 사용자: " + e.getMessage());
  }
  
  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<String> handleInvalidRequest(InvalidRequestException e) {
    log.error("handleInvalidRequest - caught exception: " + e.getMessage());
    return ResponseEntity.badRequest().body("잘못된 요청입니다: " + e.getMessage());
  }
}
