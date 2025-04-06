package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 자원을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),

    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE_DENIED(HttpStatus.FORBIDDEN, "비공개 채널은 수정할 수 없습니다."),

    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메시지를 찾을 수 없습니다."),

    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파일을를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
