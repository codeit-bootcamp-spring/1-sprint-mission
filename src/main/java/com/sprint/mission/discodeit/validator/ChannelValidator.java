package com.sprint.mission.discodeit.validator;

import org.springframework.stereotype.Component;

@Component
public class ChannelValidator implements Validator {

    public void validate(String name, String description) {
        validateName(name);
        validatedescription(description);
    }

    public void validateName(String name) {
        isBlank(name);
    }

    public void validatedescription(String description) {
        isBlank(description);
    }
}
