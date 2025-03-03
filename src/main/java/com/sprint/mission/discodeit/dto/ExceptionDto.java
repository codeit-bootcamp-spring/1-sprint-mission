package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionDto {
    private final int status;
    private final String message;

    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode.getStatus().value(), errorCode.getMessage());
    }
}
