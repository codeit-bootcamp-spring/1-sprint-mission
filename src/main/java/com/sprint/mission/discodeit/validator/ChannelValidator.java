package com.sprint.mission.discodeit.validator;

import org.springframework.stereotype.Component;

@Component
public class ChannelValidator {

    public void validateChannel(String name, String description){
        validateName(name);
        validateDescription(description);
    }

    public void validateName(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("입력 값은 null이거나 공백일 수 없습니다.");
        }
    }

    public void validateDescription(String description){
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("입력 값은 null이거나 공백일 수 없습니다.");
        }
    }

}
