package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.Objects;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;

public class CreateAtValidator {
    public void validateCreateAtFormat(Long time) throws InvalidFormatException {
        if (Objects.equals(time.intValue(), EMPTY_TIME.getValue()))
            throw new InvalidFormatException(ErrorCode.INVALID_TIME_FORMAT);
    }
}
