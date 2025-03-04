package com.sprint.mission.discodeit.validator;

public interface Validator {

    default void isBlank(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 입력이 비어있습니다.");
        }
    }
}
