package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.message.MessageMissMatchException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("서버 내부 에러: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        e.toString(),
        "서버 에러",
        null,
        e.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  // 0. 기타 오류
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    log.error("애플리케이션 예외 발생: 타입={}, 메시지={}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.warn("요청 데이터가 유효하지 않음: {}", e.getMessage());

    Map<String, Object> validationErrors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error -> {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      Object rejectedValue = error.getRejectedValue();

      Map<String, Object> fieldError = new HashMap<>();
      fieldError.put("message", errorMessage);
      fieldError.put("rejectedValue", rejectedValue);

      validationErrors.put(fieldName, fieldError);
    });

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        e.getStatusCode().toString(),
        "요청 데이터 유효성 검증에 실패했습니다.",
        validationErrors,
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  // 1. 사용자(User) 관련 예외
  // UserException
  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
    log.warn("사용자 관련 예외 발생: 타입={}, 메시지={}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  // UserNotFoundException - 존재하지 않는 사용자
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    log.warn("사용자를 찾을 수 없는 예외 발생: 타입 = {}, 메세지={} ", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  // UserAlreadyExistException - 사용자 Email, nickname 중복
  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(
      UserAlreadyExistException e) {
    log.warn("사용자 중복 예외 발생: 타입 = {}, 메세지 = {}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.CONFLICT.value());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  //2. 채널(Channel) 관련 예외
  //ChannelException
  @ExceptionHandler(ChannelException.class)
  public ResponseEntity<ErrorResponse> handleChannelException(ChannelException e) {
    log.warn("채널 관련 예외 발생: 타입={}, 메시지={}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  //ChannelNotFoundException - 존재하지 않는 채널
  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleChannelNotFoundException(ChannelNotFoundException e) {
    log.warn("채널을 찾을 수 없는 예외 발생: 타입 = {}, 메세지={} ", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  //PrivateChannelUpdateException - 비공개 채널 수정 시도
  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<ErrorResponse> handlePrivateChannelUpdateException(
      PrivateChannelUpdateException e
  ) {
    log.warn("Private 채널 수정 예외 발생: 타입= {}, 메세지={} ", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  //3. 메세지()관련 예외
  //MessageException
  @ExceptionHandler(MessageException.class)
  public ResponseEntity<ErrorResponse> handleMessageException(MessageException e) {
    log.warn("메세지 관련 예외 발생: 타입={}, 메시지={}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  //MessageMissMatchException - 메세지 작성자(소유자)가 아닌 사용자가 수정/삭제 시도
  @ExceptionHandler(MessageMissMatchException.class)
  public ResponseEntity<ErrorResponse> handleMessageMissMatchException(
      MessageMissMatchException e) {
    log.warn("메세지 작성자 불일치 예외 발생: 타입={}, 메시지={}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.METHOD_NOT_ALLOWED.value());

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
  }

  //MessageNotFoundException - 존재하지 않는 메세지
  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> MessageNotFoundException(MessageNotFoundException e) {
    log.warn("메세지를 찾을 수 없는 예외 발생: 타입={}, 메시지={}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  //5. 메세지 읽음 상태(ReadStatus) 관련 예외
  @ExceptionHandler(ReadStatusException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusException(ReadStatusException e) {
    log.warn("메세지 읽음 상태 관련 예외 발생: 타입={}, 메시지={}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  // ReadStatusNotFoundException - 존재하지 않는 메세지 읽음 상태
  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> ReadStatusNotFoundException(
      ReadStatusNotFoundException e) {
    log.warn("메세지 읽음 상태를 찾을 수 없는 예외 발생: 타입 = {}, 메세지={} ", e.getClass().getSimpleName(),
        e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  //ReadStatusAlreadyExistException - 이미 존재하는 메세지 읽음 상태
  @ExceptionHandler(ReadStatusAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> ReadStatusAlreadyExistException(
      ReadStatusAlreadyExistException e
  ) {
    log.warn("메세지 읽음 상태 중복 예외 발생: 타입 = {}, 메세지 = {}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  //6. 파일(File, BinaryContent) 관련 예외
  @ExceptionHandler(BinaryContentException.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentException(
      BinaryContentException e
  ) {
    log.warn("파일 관련 예외 발생: 타입 = {}, 메세지 = {}", e.getClass().getSimpleName(), e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(BinaryContentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentNotFoundException(
      BinaryContentNotFoundException e
  ) {
    log.warn("존재하지 않는 파일 예외 발생: 타입 = {}, 메세지 = {}", e.getClass().getSimpleName(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

}
