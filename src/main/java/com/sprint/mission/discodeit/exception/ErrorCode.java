package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  //유저 관련
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "존재하지 않는 사용자입니다."),
  INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "USER_002", "비밀번호가 일치하지 않습니다."),
  DUPLICATED_USERNAME(HttpStatus.CONFLICT, "USER_003", "이미 사용 중인 사용자명입니다."),
  INVALID_USER_INPUT(HttpStatus.BAD_REQUEST, "USER_004", "사용자 정보가 유효하지 않습니다."),
  DUPLICATED_EMAIL(HttpStatus.CONFLICT, "USER_005", "이미 사용 중인 이메일입니다."),

  //파일
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_001", "파일을 찾을 수 없습니다."),
  INVALID_FILE_DATA(HttpStatus.BAD_REQUEST, "FILE_002", "파일 데이터가 유효하지 않습니다."),
  FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_003", "파일 저장에 실패했습니다."),

  //채널
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL_001", "채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "CHANNEL_002", "비공개 채널은 수정할 수 없습니다."),

  //메시지
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE_001", "메시지를 찾을 수 없습니다."),

  //읽음 상태
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "READ_001", "읽음 상태 정보를 찾을 수 없습니다."),
  USER_ALREADY_MEMBER(HttpStatus.CONFLICT, "READ_002", "사용자는 이미 해당 채널의 멤버입니다."),

  //유저 상태
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "STATUS_001", "사용자 상태 정보를 찾을 수 없습니다."),
  USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "STATUS_002", "사용자 상태 정보가 이미 존재합니다.");

  private HttpStatus status;
  private String code;
  private String message;

  ErrorCode(HttpStatus httpStatus, String code, String message) {
    this.status = httpStatus;
    this.code = code;
    this.message = message;
  }
}
