package com.sprint.mission.discodeit.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON001" , "Server error."),

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER001", "해당 사용자가 존재하지 않습니다."),
    USER_IS_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER002", "이미 존재하는 사용자입니다."),
    USER_NAME_TOO_SHORT(HttpStatus.BAD_REQUEST, "USER003", "사용자명은 최소 2글자 입니다."),
    USER_NAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER004", "이미 사용중인 사용자명입니다."),
    USER_EMAIL_FORMAT_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER005", "이메일 형식에 맞지 않습니다."),
    USER_EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER006", "이미 사용중인 이메일입니다."),
    USER_PASSWORD_BLACK(HttpStatus.BAD_REQUEST,"USER007", "비밀번호는 비워둘 수 없습니다."),
    USER_PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "USER008", "비밀번호는 최소 6글자 입니다."),

    // CHANNEL
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL001", "해당 채널이 존재하지 않습니다."),
    CHANNEL_NAME_BLANK(HttpStatus.BAD_REQUEST, "CHANNEL002", "채널 이름은 비워둘 수 없습니다."),

    // MESSAGE
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE001", "해당 메세지가 존재하지 않습니다."),
    MESSAGE_CONTENT_BLANK(HttpStatus.BAD_REQUEST, "MESSAGE002", "메세지 내용을 비워둘 수 없습니다."),

    // LOGIN
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "AUTH001", "로그인에 실패하였습니다."),

    // BINARY_CONTENT
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BINARY001", "해당 파일이 존재하지 않습니다."),

    // READ_STATUS
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "READ_STATUS001", "해당 수신 상태 정보가 존재하지 않습니다."),
    READ_IS_ALREADY_EXIST(HttpStatus.NOT_FOUND, "READ_STATUS002", "수신 상태 정보가 이미 존재합니다."),

    // USER_STATUS
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_STATUS001", "해당 사용자 상태가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
