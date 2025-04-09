package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // User 관련 에러
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
  USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다"),
  INVALID_USER_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 사용자 인증 정보입니다"),

  // Channel 관련 에러
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다"),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "비공개 채널은 수정할 수 없습니다"),
  CHANNEL_ACCESS_DENIED(HttpStatus.FORBIDDEN, "채널 접근 권한이 없습니다"),

  // Message 관련 에러
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다"),
  MESSAGE_CONTENT_INVALID(HttpStatus.BAD_REQUEST, "메시지 내용이 유효하지 않습니다"),
  MESSAGE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "메시지 관련 작업 권한이 없습니다"),

  // BinaryContent 관련 에러
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다"),
  FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다"),
  FILE_DOWNLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드 중 오류가 발생했습니다"),
  BINARY_CONTENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 파일입니다"),

  // ReadStatus 관련 에러
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "읽음 상태 정보를 찾을 수 없습니다"),
  READ_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 읽음 상태입니다"),

  // UserStatus 관련 에러
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 상태 정보를 찾을 수 없습니다"),
  USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자 상태입니다"),

  // 파일 시스템 초기화 관련 에러
  STORAGE_INITIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "저장소 초기화 중 오류가 발생했습니다"),

  // 시스템 에러
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다"),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다");

  private final HttpStatus httpStatus;
  private final String message;
}