package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "GLOBAL_001", "해당 사용자가 존재하지 않습니다."),
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "GLOBAL_002", "해당 채널이 존재하지 않습니다."),
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "GLOBAL_003", "해당 메시지가 존재하지 않습니다."),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "GLOBAL_004", "해당 읽음 상태가 존재하지 않습니다."),
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "GLOBAL_005", "해당 사용자의 상태가 존재하지 않습니다."),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "GLOBAL_006", "입력값이 올바르지 않습니다."),
  DUPLICATED_EMAIL(HttpStatus.CONFLICT, "USER_001", "이미 사용중인 이메일입니다."),
  USER_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "USER_002", "회원가입 중 오류가 발생하였습니다."),
  AUTH_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_002", "인증 서버에서 오류가 발생했습니다."),
  PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "AUTH_001", "비밀번호가 일치하지 않습니다."),
  FILE_STORAGE_FAILED(HttpStatus.BAD_REQUEST, "BINARY_001", "파일 저장에 실패하였습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "BINARY_002", "해당 파일이 존재하지 않습니다."),
  INVALID_CHANNEL_TYPE(HttpStatus.BAD_REQUEST, "CHANNEL_001", "채널 타입이 올바르지 않습니다."),
  DUPLICATED_STATUS(HttpStatus.CONFLICT, "READ_STATUS_001", "이미 읽은 상태입니다."),
  STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "STORAGE_001", "저장소에 문제가 발생하였습니다."),
  STORAGE_NOT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "STORAGE_002", "저장소에 문제가 발생하였습니다."),
  STORAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORAGE_003", "저장소에서 파일을 찾을 수 없습니다."),
  STORAGE_NOT_DOWNLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "STORAGE_004", "다운로드를 할 수 없습니다.");


  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}