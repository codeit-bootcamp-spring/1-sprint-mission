package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.constant.MessageConstant;

public class MessageValidationException extends IllegalArgumentException {
  public MessageValidationException() {
    super(MessageConstant.MESSAGE_VALIDATION_FAIL_BASIC);
  }
}
