package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class ChannelValidator {

    public void validateChannel(String name, String description){
        validateName(name);
        validateDescription(description);
    }

    public void validateName(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INPUT_VALUE_INVALID);
        }
    }

    public void validateDescription(String description){
        if (description == null || description.trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INPUT_VALUE_INVALID);
        }
    }

}
