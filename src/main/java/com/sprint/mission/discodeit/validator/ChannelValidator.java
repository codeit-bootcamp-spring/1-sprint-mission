package com.sprint.mission.discodeit.validator;

import org.springframework.stereotype.Component;

@Component
public class ChannelValidator implements Validator {

    public void validate(String name, String introduction) {
        validateName(name);
        validateIntroduction(introduction);
    }

    public void validateName(String name) {
        isBlank(name);
    }

    public void validateIntroduction(String introduction) {
        isBlank(introduction);
    }
}
