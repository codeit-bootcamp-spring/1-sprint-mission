package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.validation.ChannelValidator;
import org.springframework.stereotype.Component;

@Component
public class ChannelValidatorImpl implements ChannelValidator {

    @Override
    public boolean isValidTitle(String title) {
        if (title.isBlank()) {
            System.out.println("title must not be blank");
            return false;
        }
        return true;
    }
}
