package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.validation.MessageValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageValidatorImpl implements MessageValidator {

    @Override
    public boolean inValidContent(String content) {
        if (content.isBlank()) {
            log.error("content must not be blank");
            return false;
        }
        return true;
    }
}
