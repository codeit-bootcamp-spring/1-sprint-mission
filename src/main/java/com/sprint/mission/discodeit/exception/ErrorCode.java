package com.sprint.mission.discodeit.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
  // user
  USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
  DUPLICATE_USER(400, "이미 존재하는 사용자입니다."),

  // channel
  CHANNEL_NOT_FOUND(404, "채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE(403, "비공개 채널을 수정할 수 없습니다."),

  //message

  //channel
  FILE_NOT_FOUND(404, "파일을 찾을 수 없습니다."),

  // login
  LOGIN_FAILED(401, "로그인에 실패하였습니다.");

  private final int status;
  private final String message;


  public int getStatus() {
    return this.status;
  }

  public String getMessage() {
    return message;
  }

}
