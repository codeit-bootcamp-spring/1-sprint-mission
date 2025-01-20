package com.sprint.mission.discodeit.validation;

import java.util.Objects;

public class ChannelValidator {
    public boolean isValidTitle(String title) {
        if (title.isBlank()) {
            System.out.println("title must not be blank");
            return false;
        }
        return true;
    }
}
