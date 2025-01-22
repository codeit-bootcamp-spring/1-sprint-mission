package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.constant.ChannelConstant;

public class ChannelValidationException extends Exception {
  public ChannelValidationException() {
    super(ChannelConstant.CHANNEL_VALIDATION_EXCEPTION_MESSAGE_BASIC);
  }
}
