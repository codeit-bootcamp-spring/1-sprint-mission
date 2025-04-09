package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("USER_NOT_FOUND")
            .message(ErrorCode.USER_NOT_FOUND.getMessage())
            .details(e.getDetails())
            .exceptionType(e.getClass().getSimpleName())
            .status(HttpStatus.NOT_FOUND.value())
            .build();
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
  }

  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("USER_ALREADY_EXISTS")
            .message(ErrorCode.DUPLICATE_USER.getMessage())
            .details(e.getDetails())
            .exceptionType(e.getClass().getSimpleName())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
  }

  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleChannelNotFoundException(ChannelNotFoundException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("CHANNEL_NOT_FOUND")
            .message(ErrorCode.CHANNEL_NOT_FOUND.getMessage())
            .details(e.getDetails())
            .exceptionType(e.getClass().getSimpleName())
            .status(HttpStatus.NOT_FOUND.value())
            .build();
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
  }
  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<ErrorResponse> handlePrivateChannelUpdateException(PrivateChannelUpdateException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("PRIVATE_CHANNEL_UPDATE")
            .message(ErrorCode.PRIVATE_CHANNEL_UPDATE.getMessage())
            .details(e.getDetails())
            .exceptionType(e.getClass().getSimpleName())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
  }
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("DISCODEIT_ERROR")
            .message(e.getMessage())
            .details(e.getDetails())
            .exceptionType(e.getClass().getSimpleName())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST) // TODO : e.getStatusCode().value()로 수정?
            .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("VALIDATION_FAILED_ERROR")
            .message("유효성 검증에 실패했습니다.")
            .exceptionType(e.getClass().getSimpleName())
            .status(e.getStatusCode().value())
            .build();
    return ResponseEntity
            .status(e.getStatusCode())
            .body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("BAD_REQUEST")
            .message(e.getMessage())
            .exceptionType(e.getClass().getSimpleName())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .code("INTERNAL_SERVER_ERROR")
            .message(e.getMessage())
            .details(Map.of("error", e.getMessage()))
            .exceptionType(e.getClass().getSimpleName())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
  }
}
