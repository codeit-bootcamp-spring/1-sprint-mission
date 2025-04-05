package com.sprint.mission.discodeit.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // COMMON
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON001",
      "Server Error, 관리자에게 문의해주세요."),
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON002", "입력 값이 올바르지 않습니다."),

  // USER
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER001", "해당 사용자가 존재하지 않습니다."),
  USER_IS_ALREADY_EXIST(HttpStatus.CONFLICT, "USER002", "이미 존재하는 사용자입니다."),
  USER_EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "USER003", "이미 사용중인 이메일입니다."),

  // CHANNEL
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL001", "해당 채널이 존재하지 않습니다."),
  PRIVATE_CHANNEL_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "CHANNEL002",
      "Private 채널은 수정될 수 없습니다."),

  // MESSAGE
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE001", "해당 메세지가 존재하지 않습니다."),

  // LOGIN
  LOGIN_FAILED(HttpStatus.BAD_REQUEST, "AUTH001", "로그인에 실패하였습니다."),

  // BINARY_CONTENT
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BINARY001", "해당 파일이 존재하지 않습니다."),
  BINARY_STORAGE_INIT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "BINARY002",
      "바이너리 저장소 초기화에 실패했습니다."),
  BINARY_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "BINARY003", "바이너리 콘텐츠 저장에 실패했습니다."),
  BINARY_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "BINARY004", "바이너리 콘텐츠를 읽을 수 없습니다."),
  STREAM_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "BINARY005", "스트림 생성에 실패했습니다."),

  // READ_STATUS
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "READ_STATUS001", "해당 수신 상태 정보가 존재하지 않습니다."),
  READ_IS_ALREADY_EXIST(HttpStatus.CONFLICT, "READ_STATUS002", "수신 상태 정보가 이미 존재합니다."),

  // USER_STATUS
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_STATUS001", "해당 사용자 상태가 존재하지 않습니다."),
  USER_STATUS_IS_ALREADY_EXIST(HttpStatus.CONFLICT, "USER_STATUS002", "사용자 상태가 이미 존재합니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
