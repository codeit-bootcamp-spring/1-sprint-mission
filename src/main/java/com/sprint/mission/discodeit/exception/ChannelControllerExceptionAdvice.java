package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelCanNotModifyException;
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
  public ErrorResponse channelNotFound(ChannelNotFoundException e) {
    log.error("Channel exception: {}", e.getMessage());

    return new ErrorResponse(e.getMessage());
  }

  /**
   * 비공개 채널은 수정할 수 없음
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(PrivateChannelCanNotModifyException.class)
  public ErrorResponse channelIllegal(PrivateChannelCanNotModifyException e) {
    log.error("Channel exception: {}", e.getMessage());

    return new ErrorResponse(e.getMessage());
  }
}
