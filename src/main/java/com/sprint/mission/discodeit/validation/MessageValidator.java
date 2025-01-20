package com.sprint.mission.discodeit.validation;

public class MessageValidator {
    public boolean inValidContent(String content) {
        if (content.isBlank()) {
            System.out.println("content must not be blank");
            return false;
        }
        return true;
    }
}
