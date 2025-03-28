package com.sprint.mission.discodeit.exception;

public enum ErrorCode {
  //User
  USER_NOT_FOUND(404, "USER_001", "사용자를 찾을 수 없습니다."),
  DUPLICATE_USER(409, "USER_002", "이미 존재하는 사용자입니다."),
  DUPLICATE_EMAIL(409, "USER_003", "이미 등록된 이메일입니다."),
  //Channel
  CHANNEL_NOT_FOUND(404, "CHANNEL_001", "채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED(404, "CHANNEL-010", "비공개 채널은 수정할 수 없습니다."),
  //Message
  MESSAGE_NOT_FOUND(404, "MESSAGE_001", "메시지를 찾을 수 없습니다."),
  //BinaryContent
  BINARYCONTENT_NOT_FOUND(404, "BINARYCONTENT_001", "파일을 찾을 수 없습니다."),
  //ReadStatus
  READSTATUS_NOT_FOUND(404, "READSTATUS_001", "ReadStatus 찾을 수 없습니다."),
  READ_STATUS_ALREADY_EXISTS(409, "READSTATUS_002", "이미 해당 유저의 읽기 상태가 존재합니다."),
  //UserStatus
  USER_STATUS_NOT_FOUND(404, "USER_STATUS_001", "UserStatus 를 찾을 수 없습니다."),
  USER_STATUS_ALREADY_EXISTS(409, "USER_004", "해당 유저의 상태 정보가 이미 존재합니다.");
  private final int status;
  private final String code;
  private final String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }
}