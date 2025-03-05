package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.message.ChannelAuthorNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.message.NullMessageContentException;
import com.sprint.mission.discodeit.exception.message.NullMessageTitleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MessageControllerExceptionAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(MessageNotFoundException.class)
  public ErrorResponse messageNotFound(MessageNotFoundException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NullMessageContentException.class)
  public ErrorResult messageNullContent(NullMessageContentException e) {
    log.error("Message exception: {}", e.getMessage());

    return new ErrorResult(ErrorCode.MESSAGE_NULL_CONTENT);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NullMessageTitleException.class)
  public ErrorResult messageNullContent(NullMessageTitleException e) {
    log.error("Message exception: {}", e.getMessage());

    return new ErrorResult(ErrorCode.MESSAGE_NULL_TITLE);
  }


  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ChannelAuthorNotFoundException.class)
  public ErrorResponse messageAuthorNotFound(ChannelAuthorNotFoundException e) {
    return new ErrorResponse(e.getMessage());
  }
}
