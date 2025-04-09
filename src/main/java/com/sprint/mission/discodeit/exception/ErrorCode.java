package com.sprint.mission.discodeit.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {

  //TODO: 기회되면 코드에 자체 코드 ex) U001, C001.. 넣기

  // user
  USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
  DUPLICATE_USER(400, "이미 존재하는 사용자입니다."),

  // channel
  CHANNEL_NOT_FOUND(404, "채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE(403, "비공개 채널을 수정할 수 없습니다."),

  //message
  MESSAGE_NOT_FOUND(404, "메시지를 찾을 수 없습니다."),

  //channel
  FILE_NOT_FOUND(404, "파일을 찾을 수 없습니다."),

  // login
  LOGIN_FAILED(401, "로그인에 실패하였습니다."),

  //기존 에러코드 커스텀
  ELEMENTS_NOT_FOUND(404, "해당 리소스를 찾을 수 없습니다."),//no such EllementsException
  ILLEGAL_ARGUMENT(400, "잘못된 리소스가 전달되었습니다.");//IllegalArgumentException

  private final int status;
  private final String message;


  public int getStatus() {
    return this.status;
  }

  public String getMessage() {
    return message;
  }

}
