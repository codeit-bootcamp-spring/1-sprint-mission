package com.sprint.mission.discodeit.exception;

public enum ErrorCode {
  USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
  DUPLICATE_USER("중복된 사용자입니다."),
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE("비공개 채널을 수정할 수 없습니다."),
  LOGIN_FAILED("로그인에 실패하였습니다.");

  final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
