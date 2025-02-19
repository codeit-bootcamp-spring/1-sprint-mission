package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.validation.ChannelValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChannelValidatorImpl implements ChannelValidator {

    @Override
    public boolean isValidTitle(String title) {
        if (title.isBlank()) {
            log.error("title must not be blank");
            return false;
        }
        return true;
    }
}
