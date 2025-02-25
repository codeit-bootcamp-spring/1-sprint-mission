package com.sprint.mission.discodeit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  REQUIRED_FIELD_EMPTY(HttpStatus.BAD_REQUEST, "필수 입력 필드가 비어있습니다."),
  DEFAULT_ERROR_MESSAGE(HttpStatus.BAD_REQUEST, "허용되지 않은 작업입니다."),
  PASSWORD_MATCH_ERROR(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
  IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾지 못했습니다."),
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
  ERROR_INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Invalid email format."),
  PRIVATE_CHANNEL_CANNOT_BE_UPDATED(HttpStatus.FORBIDDEN, "private 채널은 수정할 수 없습니다.");

  private final HttpStatus status;
  private final String message;
}
