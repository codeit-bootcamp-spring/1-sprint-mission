package com.sprint.mission.discodeit.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultResponse<T> {
    private int code;
    private String message;
    private T content;

    public static <T>ResultResponse<T> of(ResultCode resultCode, T content) {
        return new ResultResponse<>(resultCode.getCode(), resultCode.getMessage(), content);
    }
}
