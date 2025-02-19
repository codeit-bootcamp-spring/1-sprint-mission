package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    //stauts : 상태코드
    //error : 에러코드
    //code : enum name
    //message : enum 메시지
    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().name();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }
}
