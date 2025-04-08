package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  RESOURCE_NOT_FOUND("INTERNAL_SERVER_ERROR", "잘못된 요청입니다."),
  MESSAGE_NOT_FOUND("NOT_FOUND", "메세지를 찾을 수 없습니다."),
  CHANNEL_NOT_FOUND("NOT_FOUND", "채널을 찾을 수 없습니다."),
  BINARY_CONTENT_NOT_FOUND("NOT_FOUND", "Binary Content를 찾을 수 없습니다."),
  USER_NOT_FOUND("NOT_FOUND", "유저를 찾을 수 없습니다."),
  DUPLICATE_USER_NAME("CONFLICT", "중복된 유저 이름입니다."),
  DUPLICATE_USER_EMAIL("CONFLICT", "중복된 이메일입니다."),
  DATA_INTEGRITY_VIOLATION("CONFLICT", "중복된 아이디와 이메일입니다."),
  READ_STATUS_NOT_FOUND("NOT_FOUND", "User ReadStatus를 찾을 수 없습니다."),
  EXIST_READ_STATUS("BAD_REQUEST", "User ReadStatus가 이미 존재합니다."),
  USER_STATUS_NOT_FOUND("NOT_FOUND", "User ReadStatus를 찾을 수 없습니다."),
  USER_STATUS_NOT_NULL("BAD_REQUEST", "시간 정보를 확인해주세요."),
  USER_AUTH_FAIL("BAD_REQUEST", "아이디 또는 비밀번호가 잘못되었습니다."),
  EXIST_USER("BAD_REQUEST", "존재하는 유저입니다."),
  MESSAGE_NULL_CONTENT("BAD_REQUEST", "메세지 내용이 비어있습니다."),
  MESSAGE_NULL_TITLE("BAD_REQUEST", "메세지 제목이 비어있습니다."),
  ILLEGAL_USER("BAD_REQUEST", "잘못된 유저 정보입니다."),
  ILLEGAL_CHANNEL("BAD_REQUEST", "잘못된 채널 정보입니다."),
  INTERNAL_SERVER_ERROR_BINARY("INTERNAL_SERVER_ERROR", "파일에 문제가 생겼습니다."),
  INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버에 문제가 생겼습니다."),
  NOT_SAVED_FILE("INTERNAL_SERVER_ERROR", "서버에 문제가 생겼습니다."),
  FAIL_READ_FILE("INTERNAL_SERVER_ERROR", "서버에 문제가 생겼습니다."),
  FAIL_BEAN_VALIDATE("BAD_REQUEST", "잘못된 데이터입니다."),
  MODIFY_PRIVATE_CHANNEL("BAD_REQUEST", "Private Channel 수정할 수 없습니다.");

  private final String code;

  @Getter
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

}
