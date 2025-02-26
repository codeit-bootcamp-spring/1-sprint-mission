package com.sprint.mission.discodeit.exception.channel;

public class IllegalChannelException extends RuntimeException {

  public IllegalChannelException() {
    super();
  }

  public IllegalChannelException(String channelId) {
    super(channelId);
  }
}
