package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  //User
  USER_NOT_FOUND("유저를 찾을 수 없습니다."),
  USER_DUPLICATE_USERNAME("유저 USERNAME 중복입니다."),
  USER_DUPLICATE_EMAIL("유저 EMAIL 중복입니다."),
  USER_PASSWORD_INCORRECT("유저 PASSWORD 가 일치하지 않습니다."),
  //Channel
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_CANNOT_UPDATE("PRIVATE 채널은 업데이트 할 수 없습니다."),

  //Message
  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),

  //BinaryContent
  BINARYCONENT_NOT_FOUND("BINARY CONTENT를 찾을 수 없습니다."),
  NULL_FILE_INFO("FILE 중 null이 있습니다."),

  //ReadStatus
  READSTATUS_NOT_FOUND("READSTATUS를 찾을 수 없습니다."),
  EXIST_USERID_OR_CHANNELID("USERID OR CHANNELID 가 이미 존재합니다."),

  //UserStatus
  USERSTATUS_NOT_FOUND("USERSTATUS를 찾을 수 없습니다."),
  USERSTATUS_ALREADY_EXIST("USERSTATUS 가 이미 존재합니다.");
  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
