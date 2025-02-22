package com.sprint.mission.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_VALID_ARGUMENT(HttpStatus.BAD_REQUEST, "바디의 파라미터가 적절하지 못합니다."),
    NO_SUCH_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),
    NO_SUCH_CHANNEL(HttpStatus.NOT_FOUND, "존재하지 않는 채널입니다"),
    NO_SUCH_MESSAGE(HttpStatus.NOT_FOUND, "존재하지 않는 메시지입니다"),
    NO_SUCH_USER_STATUS(HttpStatus.NOT_FOUND, "존재하지 않는 유저 상태입니다"),
    NO_SUCH_READ_STATUS(HttpStatus.NOT_FOUND, "존재하지 않는 READ 상태입니다"),

    NO_SUCH_STATUS_MATCHING_USER(HttpStatus.NOT_FOUND, "유저 id에 맞는 상태 정보가 존재하지 않습니다"),
    NO_SUCH_PROFILE_MATCHING_USER(HttpStatus.NOT_FOUND, "유저 id에 맞는 프로필정보가 존재하지 않습니다"),
    NO_SUCH_BINARY_MATCHING_MESSAGE(HttpStatus.NOT_FOUND, "메세지 id에 맞는 Binary 데이터가 존재하지 않습니다"),
    NO_SUCH_USER_MATCHING_NAME(HttpStatus.NOT_FOUND, "존재하지 않는 유저 이름입니다"),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),

    CANNOT_REQUEST_LAST_READ_TIME(HttpStatus.FORBIDDEN, "Public 채널은 유저별 lastReadTime 호출 불가"),
    CANNOT_UPDATE_PRIVATE_CHANNEL(HttpStatus.FORBIDDEN, "Private 채널은 수정 불가능입니다."),
    ALREADY_EXIST_USER_STATUS(HttpStatus.CONFLICT, "이미 존재하는 유저 상태입니다"),
    ALREADY_EXIST_READ_STATUS(HttpStatus.CONFLICT, "이미 존재하는 READ 상태입니다"),

    ALREADY_EXIST_NAME(HttpStatus.CONFLICT, "이미 존재하는 이름입니다"),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다");
    private final HttpStatus status;
    private final String message;
}
