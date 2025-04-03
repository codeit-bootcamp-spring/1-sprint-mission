package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST,  "해당 회원이 이미 존재합니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST,  "개인 채널은 수정할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
