package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_USERNAME("USER_002", "존재하는 사용자 이름입니다.", HttpStatus.CONFLICT),
  DUPLICATE_EMAIL("USER_003", "존재하는 이메일입니다.", HttpStatus.CONFLICT),
  CHANNEL_NOT_FOUND("CHANNEL_001", "채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  PRIVATE_CHANNEL_UPDATE("CHANNEL_002", "비밀 채널은 수정할 수 없습니다.", HttpStatus.FORBIDDEN),
  MESSAGE_NOT_FOUND("MESSAGE_001", "메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  INVALID_CREDENTIALS("AUTH_001", "잘못된 사용자 이름 또는 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
  BINARY_CONTENT_NOT_FOUND("BINARY_CONTENT_001", "컨텐츠를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  READ_STATUS_NOT_FOUND("READ_STATUS_001", "읽음 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  READ_STATUS_ALREADY_EXISTS("READ_STATUS_002", "존재하는 읽음 상태입니다.", HttpStatus.CONFLICT),
  USER_STATUS_NOT_FOUND("USER_STATUS_001", "사용자 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_STATUS_ALREADY_EXISTS("USER_STATUS_002", "존재하는 사용자 상태입니다.", HttpStatus.CONFLICT);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
