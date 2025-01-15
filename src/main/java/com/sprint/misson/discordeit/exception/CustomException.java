package com.sprint.misson.discordeit.exception;

import com.sprint.misson.discordeit.code.ErrorCode;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    //기본 사용
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    //상세메세지와 함께 사용
    //리팩토링 필요
    //TODO - 고민
    //ErrorCode에 이미 이유가 적혀있는데, 굳이 detail 메세지를 직접 작성할 필요가 있을까?
    public CustomException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
