package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    DUPLICATE_USER("이미 존재하는 사용자입니다."),
    CHANNEL_NOT_FOUND("존재하지 않는 채널입니다."),
    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),
    MESSAGE_NOT_FOUND("존재하지 않는 메시지입니다."),
    INVALID_FILE("잘못된 파일 형식입니다."),
    UNAUTHORIZED("인증되지 않은 요청입니다."),
    READ_STATUS_NOT_FOUND("존재하지 않는 읽음 상태입니다."),
    READ_STATUS_ALREADY_EXISTS("이미 존재하는 읽음 상태입니다."),
    USER_STATUS_ALREADY_EXISTS("이미 사용자 상태가 존재합니다."),
    USER_STATUS_NOT_FOUND("존재하지 않는 사용자 상태입니다.");

    private final String message;
}
