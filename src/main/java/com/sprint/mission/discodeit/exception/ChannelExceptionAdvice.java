package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelCanNotModifyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ChannelExceptionAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ChannelNotFoundException.class)
  public ErrorResponse channelNotFound(ChannelNotFoundException e) {
    log.error("Channel exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.NOT_FOUND.value())
        .build();
  }

  /**
   * 비공개 채널은 수정할 수 없음
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(PrivateChannelCanNotModifyException.class)
  public ErrorResponse channelIllegal(PrivateChannelCanNotModifyException e) {
    log.error("Channel exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }
}
