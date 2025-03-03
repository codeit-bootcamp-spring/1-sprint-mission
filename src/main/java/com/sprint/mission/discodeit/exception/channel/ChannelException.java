package com.sprint.mission.discodeit.exception.channel;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ChannelException extends RuntimeException {

  @Getter
  private final HttpStatus status;

  public ChannelException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
