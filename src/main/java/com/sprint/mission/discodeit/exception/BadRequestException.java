package com.sprint.mission.discodeit.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException{
    private final ErrorCode errorCode;
}
