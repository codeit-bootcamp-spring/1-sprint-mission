package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.validation.MessageValidator;
import org.springframework.stereotype.Component;

@Component
public class MessageValidatorImpl implements MessageValidator {

    @Override
    public boolean inValidContent(String content) {
        if (content.isBlank()) {
            System.out.println("content must not be blank");
            return false;
        }
        return true;
    }
}
