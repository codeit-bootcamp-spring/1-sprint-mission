package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException() {
    super(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND, "Channel not found");
  }
}