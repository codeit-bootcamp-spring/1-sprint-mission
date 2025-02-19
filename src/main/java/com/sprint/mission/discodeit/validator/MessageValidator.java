package com.sprint.mission.discodeit.validator;

import org.springframework.stereotype.Component;

@Component
public class MessageValidator implements Validator {

    public void validate(String content) {
        validateContent(content);
    }

    public void validateContent(String content) {
        isBlank(content);
    }
}
