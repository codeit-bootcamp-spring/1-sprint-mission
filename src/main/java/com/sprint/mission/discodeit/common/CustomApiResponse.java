package com.sprint.mission.discodeit.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionDto;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public record CustomApiResponse<T>(
        @JsonIgnore
        HttpStatus httpStatus,
        boolean success,
        @Nullable T data,
        @Nullable ExceptionDto error
) {
    public static <T> CustomApiResponse<T> ok(@Nullable final T data) {
        return new CustomApiResponse<>(HttpStatus.OK, true, data, null);
    }

    public static <T> CustomApiResponse<T> created(@Nullable final T data) {
        return new CustomApiResponse<>(HttpStatus.CREATED, true, data, null);
    }

    public static <T> CustomApiResponse<T> fail(final CustomException e) {
        return new CustomApiResponse<>(e.getErrorCode().getStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
    }
}
