package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException() {
    super(Instant.now(), ErrorCode.PRIVATE_CHANNEL_UPDATE, "Private Channel Updated");
  }
}