package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // User
  USER_NOT_FOUND("유저를 찾을 수 없습니다."),
  DUPLICATE_EMAIL("중복된 이메일입니다."),
  DUPLICATE_USERNAME("중복된 유저 이름입니다."),

  // Channel
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  DUPLICATE_CHANNEL_NAME("중복된 채널 이름입니다."),
  PRIVATE_CHANNEL_UPDATE("개인 채널은 수정할 수 없습니다."),

  // Message
  MESSAGE_NOT_FOUND("메세지를 찾을 수 없습니다."),

  // Auth
  INVALID_PASSWORD("잘못된 비밀번호입니다."),

  // BinaryContent
  BINARYCONTENT_NOT_FOUND("파일을 찾을 수 없습니다."),

  // ReadStatus
  READSTATUS_NOT_FOUND("읽음 정보를 찾을 수 없습니다."),
  DUPLICATE_READSTATUS("중복된 읽음 정보입니다."),

  // UserStatus
  USERSTATUS_NOT_FOUND("유저 상태 정보를 찾을 수 없습니다."),
  DUPLICATE_USERSTATUS("중복된 유저 상태 정보입니다.");

  private String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
