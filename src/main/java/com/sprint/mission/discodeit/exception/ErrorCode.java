package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.", "U001"),
  DUPLICATE_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 유저 입니다.", "U002"),

  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채널을 찾을 수 없습니다.", "C001"),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "Private 채널은 변경할 수 없습니다.", "C002"),

  DUPLICATE_READ_STATUS(HttpStatus.BAD_REQUEST, "이미 존재하는 읽음 상태 입니다.", "R001"),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 읽음 상태를 찾을 수 없습니다.", "R002"),

  UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의해주세요.", "S001"),
  ;

  private final HttpStatus status;
  private final String message;
  private final String code;

  ErrorCode(HttpStatus status, String message, String code) {
    this.status = status;
    this.message = message;
    this.code = code;
  }

}
