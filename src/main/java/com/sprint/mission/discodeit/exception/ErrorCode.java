// ErrorCode Enum 클래스
package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
  USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "사용자 이름을 찾을 수 없습니다."),
  USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "이미 존재하는 사용자입니다."),
  USER_STATUS_NOT_FOUND("USER_STATUS_NOT_FOUND", "사용 상태를 찾을 수 없습니다."),
  MESSAGE_NOT_FOUND("MESSAGE_NOT_FOUND", "메시지를 찾을 수 없습니다."),
  CHANNEL_NOT_FOUND("CHANNEL_NOT_FOUND", "채널을 찾을 수 없습니다."),
  READ_STATUS_ALREADY_EXISTS("READ_STATUS_ALREADY_EXISTS", "이미 존재하는 읽음 상태입니다."),
  BINARY_CONTENT_NOT_FOUND("BINARY_CONTENT_NOT_FOUND", "컨텐츠를 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED("PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED", "비공개 채널은 수정할 수 없습니다."),
  INVALID_ARGUMENT("INVALID_ARGUMENT", "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생하였습니다.");

  private final String code;
  private final String message;
}
