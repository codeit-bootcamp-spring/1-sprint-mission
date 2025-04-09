package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundExeption;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleException(NoSuchElementException e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleException(IllegalArgumentException e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(e.getMessage());
  }

  // 검증 실패 시 발생하는 예외처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();

    // 필드 오류 목록
    Map<String, Object> validationErrors = bindingResult.getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            fieldError -> fieldError.getField(), //필드이름
            fieldError -> fieldError.getDefaultMessage() //오류 메시지
        ));

    ErrorResponse err = new ErrorResponse(
        Instant.now(),
        "VALIDATION_ERROR",
        "검증 오류가 발생했습니다.",
        validationErrors, // 필드별 오류 메시지
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );
    return new ResponseEntity<>(err, HttpStatus.valueOf(err.getStatus()));
  }


  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
    ErrorResponse err = new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().name(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        getHttpStatusByErrorCode(e.getErrorCode())
    );

    return new ResponseEntity<>(err, HttpStatus.valueOf(err.getStatus()));
  }

  // ErrorCode에 맞는 HTTP 상태 코드 반환
  private int getHttpStatusByErrorCode(ErrorCode errorCode) {
    switch (errorCode) {
      case USER_NOT_FOUND:
      case CHANNEL_NOT_FOUND:
      case MESSAGE_NOT_FOUND:
      case BINARYCONENT_NOT_FOUND:
      case READSTATUS_NOT_FOUND:
      case USERSTATUS_NOT_FOUND:
        return HttpStatus.NOT_FOUND.value(); // 404 Not Found
      case USER_DUPLICATE_USERNAME:
      case USER_DUPLICATE_EMAIL:
      case EXIST_USERID_OR_CHANNELID:
        return HttpStatus.BAD_REQUEST.value(); // 400 Bad Request
      case USER_PASSWORD_INCORRECT:
        return HttpStatus.UNAUTHORIZED.value(); // 401 Unauthorized
      case PRIVATE_CHANNEL_CANNOT_UPDATE:
        return HttpStatus.FORBIDDEN.value(); // 403 Forbidden
      case USERSTATUS_ALREADY_EXIST:
        return HttpStatus.CONFLICT.value(); // 409 Conflict
      default:
        return HttpStatus.INTERNAL_SERVER_ERROR.value(); // 500 Internal Server Error
    }
  }

}
