package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.validation.ChannelValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChannelValidatorImpl implements ChannelValidator {

  @Override
  public boolean isValidName(String title) {
    if (title.isBlank()) {
      throw new RestApiException(ErrorCode.CHANNEL_NAME_REQUIRED, "title=" + title);
    }
    return true;
  }
}
