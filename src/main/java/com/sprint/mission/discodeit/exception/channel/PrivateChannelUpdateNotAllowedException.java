package com.sprint.mission.discodeit.exception.channel;

import org.springframework.http.HttpStatus;

public class PrivateChannelUpdateNotAllowedException extends ChannelException {

  public PrivateChannelUpdateNotAllowedException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
