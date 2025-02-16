package com.sprint.mission.discodeit.validation.format;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_TIME;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import java.time.Instant;
import java.util.Objects;

public class CreateAtValidator {

    public void validateCreateAtFormat(Instant time) throws InvalidFormatException {
        if (Objects.equals(time, Instant.parse(EMPTY_TIME.getValue()))) {
            throw new InvalidFormatException(ErrorCode.INVALID_TIME_FORMAT);
        }
    }
}
