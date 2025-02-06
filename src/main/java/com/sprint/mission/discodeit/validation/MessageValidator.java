package com.sprint.mission.discodeit.validation;

import org.springframework.stereotype.Component;

@Component
public class MessageValidator {
    public boolean inValidContent(String content) {
        if (content.isBlank()) {
            System.out.println("content must not be blank");
            return false;
        }
        return true;
    }
}
