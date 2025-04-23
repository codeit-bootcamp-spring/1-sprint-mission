package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

  INTERNAL_SERVER_ERROR(500, "예상치 못한 오류가 발생했습니다."),

  // validation
  BAD_REQUEST(400, "입력값이 올바르지 않습니다."),

  // User
  USER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다."),
  DUPLICATE_USER(409, "이미 존재하는 사용자입니다."),
  WRONG_PASSWORD(400, "비밀번호가 틀렸습니다."),

  // Channel
  CHANNEL_NOT_FOUND(404, "해당 채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE(400, "비공개 채널은 수정할 수 없습니다."),

  // Message
  MESSAGE_NOT_FOUND(404, "해당 메시지를 찾을 수 없습니다."),

  // BinaryContent
  BINARYCONTENT_NOT_FOUND(404, "해당 파일을 찾을 수 없습니다."),

  // ReadStatus
  READSTATUS_NOT_FOUND(404, "해당 Read Status를 찾을 수 없습니다."),
  DUPLICATION_READSTATUS(409, "해당 채널에 이미 해당 유저의 Read Status가 존재합니다."),

  // UserStatus
  USERSTATUS_NOT_FOUND(404, "해당 User Status를 찾을 수 없습니다.");

  private final int status;   // 에러 코드
  private final String message;   // 에러 상세 메시지
}
