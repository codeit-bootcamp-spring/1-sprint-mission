package com.sprint.mission.discodeit.exception.channel;

import org.springframework.http.HttpStatus;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
