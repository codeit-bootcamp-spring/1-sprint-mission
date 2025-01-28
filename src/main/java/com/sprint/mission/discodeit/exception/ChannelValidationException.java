package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.constant.ChannelConstant;

public class ChannelValidationException extends IllegalArgumentException {
  public ChannelValidationException() {
    super(ChannelConstant.CHANNEL_VALIDATION_EXCEPTION_MESSAGE_BASIC);
  }

  public ChannelValidationException(String message){
    super(message);
  }
}
