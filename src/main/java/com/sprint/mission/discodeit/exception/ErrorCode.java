package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

  // server
  INVALID_REQUEST(400, "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
  VALIDATION_ERROR(400, "요청 데이터 유효성 검사에 실패했습니다."),

  // User
  USER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다."),
  DUPLICATE_USER(409, "이미 존재하는 사용자입니다."),
  INVALID_USER_CREDENTIALS(400, "잘못된 사용자 인증 정보입니다."),

  // Channel
  CHANNEL_NOT_FOUND(404, "해당 채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE(400, "비공개 채널은 수정할 수 없습니다."),

  // Message
  MESSAGE_NOT_FOUND(404, "해당 메시지를 찾을 수 없습니다."),

  // BinaryContent
  BINARY_CONTENT_NOT_FOUND(404, "해당 바이너리 컨텐츠를 찾을 수 없습니다."),

  // ReadStatus
  READ_STATUS_NOT_FOUND(404, "해당 읽음 상태를 찾을 수 없습니다."),
  DUPLICATION_READ_STATUS(409, "해당 채널에 이미 해당 유저의 읽음 상태가 존재합니다."),

  // UserStatus
  USER_STATUS_NOT_FOUND(404, "해당 사용자 상태를 찾을 수 없습니다."),
  DUPLICATE_USER_STATUS(409, "이미 존재하는 사용자 상태입니다.");

  private final int status;   // 에러 코드
  private final String message;   // 에러 상세 메시지
}
