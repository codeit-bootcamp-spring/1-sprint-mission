package com.sprint.mission.discodeit.exception.channel;

import java.util.UUID;

public class ChannelNotFoundException extends RuntimeException {

  public ChannelNotFoundException() {
    super();
  }

  public ChannelNotFoundException(UUID channelId) {
    super(String.format("Channel with id %s not found", channelId));
  }
}
