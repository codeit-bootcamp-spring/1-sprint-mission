package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  USER_NOT_FOUND("등록되지 않은 user", 404),
  USER_ALREADY_EXIST("중복된 이메일 혹은 이름", 400),
  DUPLICATE_USER_EMAIL("중복된 이메일", 400),
  DUPLICATE_USER_USERNAME("중복된 이름", 400),
  AUTHENTICATION_FAIL("비밀번호가 일치하지 않음", 400),
  USER_STATUS_NOT_FOUND("등록되지 않은 userStatus", 404),

  CHANNEL_NOT_FOUND("등록되지 않은 channel", 404),
  PRIVATE_CHANNEL_UPDATE("private 채널은 수정할 수 없음", 400),

  MESSAGE_NOT_FOUND("등록되지 않은 message", 404),

  READ_STATUS_NOT_FOUND("등록되지 않은 readStatus", 404),
  READ_STATUS_ALREADY_EXIST("이미 존재하는 readStatus", 400),

  BINARY_CONTENT_NOT_FOUND("등록되지 않은 binaryContent", 404),
  DIRECTORY_CREATE_FAIL("저장 디렉토리 생성 실패", 500),
  FILE_CREATE_FAIL("파일 생성 실패", 500),
  FILE_READ_FAIL("파일 조회 실패", 500),
  FILE_DELETE_FAIL("파일 삭제 실패", 500),

  INVALID_TOKEN("유효하지 않은 토큰", 401),
  TOKEN_NOT_FOUND("토큰을 찾을 수 없음", 401),

  UNKNOWN_EXCEPTION("서버 오류", 500);

  private final String message;
  private final int status;

  ErrorCode(String message, int status) {
    this.message = message;
    this.status = status;
  }
}
