package com.sprint.mission.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_VALID_ARGUMENT(HttpStatus.BAD_REQUEST, "바디의 파라미터가 적절하지 못합니다."),
    NO_SUCH_STATUS_MATCHING_USER(HttpStatus.NOT_FOUND, "userId에 맞는 상태 정보가 존재하지 않습니다"),
    NO_SUCH_PROFILE_MATCHING_USER(HttpStatus.NOT_FOUND, "userId에 맞는 프로필정보가 존재하지 않습니다"),
    NO_SUCH_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),
    NO_SUCH_CHANNEL(HttpStatus.NOT_FOUND, "존재하지 않는 채널입니다"),
    ALREADY_EXIST_STATUS(HttpStatus.CONFLICT, "이미 존재하는 유저 상태입니다"),
    ALREADY_EXIST_NAME(HttpStatus.CONFLICT, "이미 존재하는 이름입니다"),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다");
    private final HttpStatus status;
    private final String message;
    }
