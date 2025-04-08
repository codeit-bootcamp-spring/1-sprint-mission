package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.message.ChannelAuthorNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.message.NullMessageContentException;
import com.sprint.mission.discodeit.exception.message.NullMessageTitleException;
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
public class MessageExceptionAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(MessageNotFoundException.class)
  public ErrorResponse messageNotFound(MessageNotFoundException e) {

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.NOT_FOUND.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NullMessageContentException.class)
  public ErrorResponse messageNullContent(NullMessageContentException e) {
    log.error("Message exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NullMessageTitleException.class)
  public ErrorResponse messageNullContent(NullMessageTitleException e) {
    log.error("Message exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }


  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ChannelAuthorNotFoundException.class)
  public ErrorResponse messageAuthorNotFound(ChannelAuthorNotFoundException e) {

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.NOT_FOUND.value())
        .build();
  }
}
