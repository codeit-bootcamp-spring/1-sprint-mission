package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.error.ErrorResponse;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.WrongPasswordException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // validation
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    // 검증 대상이 @Valid 하나 당 여러 필드이므로 List로 처리
    List<String> messages = e.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.BAD_REQUEST.name(),
        ErrorCode.BAD_REQUEST.getMessage(),
        Map.of("errorMessages", messages),
        e.getClass().getSimpleName(),
        ErrorCode.BAD_REQUEST.getStatus()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  // User
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.USER_NOT_FOUND.name(),
        ErrorCode.USER_NOT_FOUND.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.USER_NOT_FOUND.getStatus()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(
      UserAlreadyExistException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.DUPLICATE_USER.name(),
        ErrorCode.DUPLICATE_USER.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.DUPLICATE_USER.getStatus()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(WrongPasswordException.class)
  public ResponseEntity<ErrorResponse> handleWrongPasswordException(
      WrongPasswordException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.WRONG_PASSWORD.name(),
        ErrorCode.WRONG_PASSWORD.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.WRONG_PASSWORD.getStatus()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  // Channel
  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleChannelNotFoundException(ChannelNotFoundException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.CHANNEL_NOT_FOUND.name(),
        ErrorCode.CHANNEL_NOT_FOUND.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.CHANNEL_NOT_FOUND.getStatus()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(PrivateChannelUpdateNotAllowedException.class)
  public ResponseEntity<ErrorResponse> handlePrivateChannelUpdateNotAllowedException(
      PrivateChannelUpdateNotAllowedException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.PRIVATE_CHANNEL_UPDATE.name(),
        ErrorCode.PRIVATE_CHANNEL_UPDATE.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.PRIVATE_CHANNEL_UPDATE.getStatus()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  // Message
  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFoundException(MessageNotFoundException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.MESSAGE_NOT_FOUND.name(),
        ErrorCode.MESSAGE_NOT_FOUND.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.MESSAGE_NOT_FOUND.getStatus()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  // BinaryContent
  @ExceptionHandler(BinaryContentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentNotFoundException(
      BinaryContentNotFoundException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.BINARYCONTENT_NOT_FOUND.name(),
        ErrorCode.BINARYCONTENT_NOT_FOUND.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.BINARYCONTENT_NOT_FOUND.getStatus()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  // ReadStatus
  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusNotFoundException(
      ReadStatusNotFoundException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.READSTATUS_NOT_FOUND.name(),
        ErrorCode.READSTATUS_NOT_FOUND.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.READSTATUS_NOT_FOUND.getStatus()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(ReadStatusAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusAlreadyExistException(
      ReadStatusAlreadyExistException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.DUPLICATION_READSTATUS.name(),
        ErrorCode.DUPLICATION_READSTATUS.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.DUPLICATION_READSTATUS.getStatus()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  //UserStatus
  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserStatusNotFoundException(
      UserStatusNotFoundException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.USERSTATUS_NOT_FOUND.name(),
        ErrorCode.USERSTATUS_NOT_FOUND.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.USERSTATUS_NOT_FOUND.getStatus()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  // 그 외 오류
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.INTERNAL_SERVER_ERROR.name(),
        ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
        Map.of("errorMessage", e.getMessage()),
        e.getClass().getSimpleName(),
        ErrorCode.INTERNAL_SERVER_ERROR.getStatus()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
