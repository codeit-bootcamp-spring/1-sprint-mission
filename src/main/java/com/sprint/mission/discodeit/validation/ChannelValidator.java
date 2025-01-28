package com.sprint.mission.discodeit.validation;

public class ChannelValidator {
    public boolean isValidTitle(String title) {
        if (title.isBlank()) {
            System.out.println("title must not be blank");
            return false;
        }
        return true;
    }
}
