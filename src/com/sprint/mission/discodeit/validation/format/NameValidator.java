package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import static com.sprint.mission.discodeit.constant.IntegerConstant.MAX_NAME_LENGTH;

public class NameValidator {
    public void validateNameFormat(String name) throws InvalidFormatException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.INVALID_NAME_FORMAT);

        if (name.length() > MAX_NAME_LENGTH.getValue())
            throw new InvalidFormatException(ErrorCode.INVALID_NAME_FORMAT);
    }
}
