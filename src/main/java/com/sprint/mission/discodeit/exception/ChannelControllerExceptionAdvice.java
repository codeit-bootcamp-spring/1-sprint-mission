package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ChannelControllerExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ChannelNotFoundException.class)
    public ErrorResult channelNotFound(ChannelNotFoundException e) {
        log.error("Channel exception: {}", e.getMessage());

        return new ErrorResult(ErrorCode.CHANNEL_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalChannelException.class)
    public ErrorResult channelIllegal(IllegalChannelException e) {
        log.error("Channel exception: {}", e.getMessage());

        return new ErrorResult(ErrorCode.ILLEGAL_CHANNEL);
    }
}
