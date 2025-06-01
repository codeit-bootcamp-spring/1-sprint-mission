package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
  MISSING_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 없습니다."),

  //NOT_FOUND
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User을 찾을 수 없습니다."),
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "Channel을 찾을 수 없습니다."),
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Message를 찾을 수 없습니다."),
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "UserStatus를 찾을 수 없습니다."),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "ReadStatus를 찾을 수 없습니다."),
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BinaryContent를 찾을 수 없습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),

  //BAD_REQUEST
  USER_DUPLICATE(HttpStatus.CONFLICT, "User가 중복입니다."),
  READ_STATUS_DUPLICATE(HttpStatus.CONFLICT, "ReadStatus가 중복입니다."),
  USER_STATUS_DUPLICATE(HttpStatus.CONFLICT, "UserStatus가 중복입니다."),
  INPUT_VALUE_INVALID(HttpStatus.BAD_REQUEST, "입력값은 공백이거나 중복일 수 없습니다."),
  INPUT_VALUE_MISMATCH(HttpStatus.BAD_REQUEST, "입력값이 형식에 맞지 않습니다."),
  LOGIN_INFO_MISMATCH(HttpStatus.UNAUTHORIZED, "로그인 정보가 불일치합니다."),
  PRIVATE_CHANNEL_IMMUTABLE(HttpStatus.FORBIDDEN, "PRIVATE 채널은 수정 불가능합니다."),

  //500
  FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
  FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 읽는 중 오류가 발생했습니다."),

  //공통 에러 코드
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."), //400
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 값 검증에 실패했습니다."), //400
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."), //405
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."); //500

  private final HttpStatus httpStatus;
  private final String message;
}
