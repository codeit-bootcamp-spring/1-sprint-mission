package com.sprint.mission.discodeit.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 발생할 예외를 처리할 에러 클래스
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detailMessage;
}
