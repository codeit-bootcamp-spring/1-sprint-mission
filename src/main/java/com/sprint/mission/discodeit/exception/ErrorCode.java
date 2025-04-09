package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  //User
  USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다."),
  DUPLICATE_EMAIL("USER_002", "이미 존재하는 이메일입니다."),
  DUPLICATE_USERNAME("USER_003", "이미 존재하는 사용자 이름입니다."),

  //Channel
  CHANNEL_NOT_FOUND("CHANNEL_001", "채널을 찾을 수 없습니다."),
  CAN_NOT_UPDATE_PRIVATE_CHANNEL("CHANNEL_002", "비공개 채널을 수정할 수 없습니다."),

  //Message
  MESSAGE_NOT_FOUND("MESSAGE_001", "메세지를 찾을 수 없습니다."),

  //BinaryContent
  BINARY_CONTENT_NOT_FOUND("BINARY_001", "바이너리 콘텐츠를 찾을 수 없습니다."),
  DUPLICATE_BINARY_FILES("BINARY_002", "이미 존재하는 파일 입니다."),
  BINARY_FILE_NOT_EXISTS("BINARY_003", "파일이 존재하지 않습니다."),

  //Auth
  NOT_MATCH_PASSWORD("AUTH_001", "비밀번호가 일치 하지 않습니다."),

  //UserStatus
  USER_STATUS_NOT_FOUND("USER_STATUS_001", "사용자 상태 정보를 찾을 수 없습니다."),
  USER_STATUS_ALREADY_EXISTS("USER_STATUS_002", "해당 사용자에 대한 상태 정보가 이미 존재합니다."),

  //ReadStatus
  READ_STATUS_NOT_FOUND("READ_STATUS_001", "사용자와 채널에 대한 읽음 정보를 찾을 수 없습니다."),
  READ_STATUS_ALREADY_EXISTS("READ_STATUS_002", "해당 사용자와 채널에 대한 읽음 정보가 이미 존재합니다.");

  private final String code;
  private final String message;
}
