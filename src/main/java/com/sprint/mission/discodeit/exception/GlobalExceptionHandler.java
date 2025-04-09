package com.sprint.mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.sprint.mission.discodeit.exception.binaryContent.*;
import com.sprint.mission.discodeit.exception.channel.*;
import com.sprint.mission.discodeit.exception.message.*;
import com.sprint.mission.discodeit.exception.readStatus.*;
import com.sprint.mission.discodeit.exception.user.*;
import com.sprint.mission.discodeit.exception.userStatus.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // ===== 1. DiscodeitException 기본 핸들러 =====
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    log.warn("DiscodeitException 발생: {}, 에러코드: {}", e.getMessage(), e.getErrorCode());
    ErrorResponse errorResponse = ErrorResponse.of(e);
    return ResponseEntity
        .status(e.getErrorCode().getHttpStatus())
        .body(errorResponse);
  }

  // ===== 2. 사용자(User) 관련 예외 처리 =====

  // 사용자를 찾을 수 없는 경우
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    log.warn("사용자를 찾을 수 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 사용자가 이미 존재하는 경우
  @ExceptionHandler({UserAlreadyExistsException.class, EmailAlreadyExistsException.class})
  public ResponseEntity<ErrorResponse> handleUserExistsException(DiscodeitException e) {
    log.warn("사용자 중복 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 사용자 인증 정보가 잘못된 경우
  @ExceptionHandler(InvalidUserCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidUserCredentialsException(
      InvalidUserCredentialsException e) {
    log.warn("사용자 인증 실패: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 기타 사용자 관련 예외
  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
    log.warn("사용자 관련 예외 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // ===== 3. 채널(Channel) 관련 예외 처리 =====

  // 채널을 찾을 수 없는 경우
  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleChannelNotFoundException(ChannelNotFoundException e) {
    log.warn("채널을 찾을 수 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 비공개 채널 수정 시도 시
  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<ErrorResponse> handlePrivateChannelUpdateException(
      PrivateChannelUpdateException e) {
    log.warn("비공개 채널 수정 시도: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 채널 접근 권한 없는 경우
  @ExceptionHandler(ChannelAccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleChannelAccessDeniedException(
      ChannelAccessDeniedException e) {
    log.warn("채널 접근 권한 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 기타 채널 관련 예외
  @ExceptionHandler(ChannelException.class)
  public ResponseEntity<ErrorResponse> handleChannelException(ChannelException e) {
    log.warn("채널 관련 예외 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // ===== 4. 메시지(Message) 관련 예외 처리 =====

  // 메시지를 찾을 수 없는 경우
  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFoundException(MessageNotFoundException e) {
    log.warn("메시지를 찾을 수 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 메시지 내용이 유효하지 않은 경우
  @ExceptionHandler(MessageContentInvalidException.class)
  public ResponseEntity<ErrorResponse> handleMessageContentInvalidException(
      MessageContentInvalidException e) {
    log.warn("메시지 내용 유효하지 않음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 메시지 관련 작업 권한이 없는 경우
  @ExceptionHandler(MessagePermissionDeniedException.class)
  public ResponseEntity<ErrorResponse> handleMessagePermissionDeniedException(
      MessagePermissionDeniedException e) {
    log.warn("메시지 관련 작업 권한 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 기타 메시지 관련 예외
  @ExceptionHandler(MessageException.class)
  public ResponseEntity<ErrorResponse> handleMessageException(MessageException e) {
    log.warn("메시지 관련 예외 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // ===== 5. 바이너리 콘텐츠(파일) 관련 예외 처리 =====

  // 파일을 찾을 수 없는 경우
  @ExceptionHandler(BinaryContentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentNotFoundException(
      BinaryContentNotFoundException e) {
    log.warn("파일을 찾을 수 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 파일 업로드 오류
  @ExceptionHandler(FileUploadException.class)
  public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException e) {
    log.error("파일 업로드 오류 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 파일 다운로드 오류
  @ExceptionHandler(FileDownloadException.class)
  public ResponseEntity<ErrorResponse> handleFileDownloadException(FileDownloadException e) {
    log.error("파일 다운로드 오류 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 파일이 이미 존재하는 경우
  @ExceptionHandler(BinaryContentAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentAlreadyExistsException(
      BinaryContentAlreadyExistsException e) {
    log.warn("파일이 이미 존재함: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 기타 파일 관련 예외
  @ExceptionHandler(BinaryContentException.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentException(BinaryContentException e) {
    log.warn("파일 관련 예외 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 파일 업로드 크기 제한 예외 처리 (Spring의 기본 예외)
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException e) {
    log.error("파일 크기 제한 초과: {}", e.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(ErrorCode.FILE_UPLOAD_ERROR.name())
        .message("파일 크기가 제한을 초과했습니다")
        .exceptionType(e.getClass().getSimpleName())
        .status(ErrorCode.FILE_UPLOAD_ERROR.getHttpStatus().value())
        .build();

    return ResponseEntity
        .status(ErrorCode.FILE_UPLOAD_ERROR.getHttpStatus())
        .body(errorResponse);
  }

  // ===== 6. 읽음 상태(ReadStatus) 관련 예외 처리 =====

  // 읽음 상태 정보를 찾을 수 없는 경우
  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusNotFoundException(
      ReadStatusNotFoundException e) {
    log.warn("읽음 상태 정보를 찾을 수 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 읽음 상태가 이미 존재하는 경우
  @ExceptionHandler(ReadStatusAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusAlreadyExistsException(
      ReadStatusAlreadyExistsException e) {
    log.warn("읽음 상태가 이미 존재함: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 기타 읽음 상태 관련 예외
  @ExceptionHandler(ReadStatusException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusException(ReadStatusException e) {
    log.warn("읽음 상태 관련 예외 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // ===== 7. 사용자 상태(UserStatus) 관련 예외 처리 =====

  // 사용자 상태 정보를 찾을 수 없는 경우
  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserStatusNotFoundException(
      UserStatusNotFoundException e) {
    log.warn("사용자 상태 정보를 찾을 수 없음: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 사용자 상태가 이미 존재하는 경우
  @ExceptionHandler(UserStatusAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserStatusAlreadyExistsException(
      UserStatusAlreadyExistsException e) {
    log.warn("사용자 상태가 이미 존재함: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // 기타 사용자 상태 관련 예외
  @ExceptionHandler(UserStatusException.class)
  public ResponseEntity<ErrorResponse> handleUserStatusException(UserStatusException e) {
    log.warn("사용자 상태 관련 예외 발생: {}", e.getMessage());
    return handleDiscodeitException(e);
  }

  // ===== 8. 일반적인 예외 처리 =====

  // 검증 실패 시
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.warn("입력값 검증 실패: {}", e.getMessage());

    Map<String, Object> details = new HashMap<>();
    for (var error : e.getBindingResult().getFieldErrors()) {
      details.put(error.getField(), error.getDefaultMessage());
    }

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(ErrorCode.INVALID_REQUEST.name())
        .message("입력값 검증에 실패했습니다")
        .status(ErrorCode.INVALID_REQUEST.getHttpStatus().value())
        .exceptionType(e.getClass().getSimpleName())
        .details(details)
        .build();

    return ResponseEntity
        .status(ErrorCode.INVALID_REQUEST.getHttpStatus())
        .body(errorResponse);
  }


  // 요소를 찾을 수 없는 경우 (일반)
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    log.warn("요소를 찾을 수 없음: {}", e.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(ErrorCode.USER_NOT_FOUND.name())
        .message(e.getMessage() != null ? e.getMessage() : "요청한 리소스를 찾을 수 없습니다")
        .exceptionType(e.getClass().getSimpleName())
        .status(ErrorCode.USER_NOT_FOUND.getHttpStatus().value())
        .build();

    return ResponseEntity
        .status(ErrorCode.USER_NOT_FOUND.getHttpStatus())
        .body(errorResponse);
  }

  // 잘못된 인자가 전달된 경우
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn("잘못된 인자 전달: {}", e.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(ErrorCode.INVALID_REQUEST.name())
        .message(e.getMessage() != null ? e.getMessage() : "잘못된 요청입니다")
        .exceptionType(e.getClass().getSimpleName())
        .status(ErrorCode.INVALID_REQUEST.getHttpStatus().value())
        .build();

    return ResponseEntity
        .status(ErrorCode.INVALID_REQUEST.getHttpStatus())
        .body(errorResponse);
  }

  // 데이터베이스 관련 오류
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
    log.error("데이터베이스 오류 발생: {}", e.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(ErrorCode.DATABASE_ERROR.name())
        .message("데이터베이스 오류가 발생했습니다")
        .exceptionType(e.getClass().getSimpleName())
        .status(ErrorCode.DATABASE_ERROR.getHttpStatus().value())
        .build();

    return ResponseEntity
        .status(ErrorCode.DATABASE_ERROR.getHttpStatus())
        .body(errorResponse);
  }

  // 기타 모든 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("서버 내부 오류 발생: {}", e.getMessage(), e);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
        .message("서버 내부 오류가 발생했습니다")
        .exceptionType(e.getClass().getSimpleName())
        .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
        .build();

    return ResponseEntity
        .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
        .body(errorResponse);
  }


}