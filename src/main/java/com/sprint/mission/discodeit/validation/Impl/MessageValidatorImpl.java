package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.validation.MessageValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageValidatorImpl implements MessageValidator {

  @Override
  public boolean inValidContent(String content) {
    if (content.isBlank()) {
//      throw new BusinessException(ErrorCode.MESSAGE_CONTENT_REQUIRED, "newContent=" + content);
    }
    return true;
  }
}
