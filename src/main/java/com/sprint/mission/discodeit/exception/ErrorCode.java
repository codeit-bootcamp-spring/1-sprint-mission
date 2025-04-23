package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // Validation 에러 추가
  VALIDATION_FAILED(HttpStatus.NOT_FOUND, "입력값 검증에 실패했습니다."),

  // 유저
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."), // 404
  USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자 이름입니다."), // 409
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

  // 채널
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."), // 404
  CHANNEL_MODIFICATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "채널의 수정을 허용하지 않습니다."), // 403

  // 메세지
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메세지를 찾을 수 없습니다."),

  // 읽음 상태
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "메세지 읽음 상태를 찾을 수 없습니다."),
  READ_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 메세지 읽음 상태입니다."),

  // 유저 상태
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 상태를 찾을 수 없습니다."),

  // 파일
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

}
