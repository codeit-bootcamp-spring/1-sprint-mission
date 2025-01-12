package com.sprint.mission.discodeit.validation.common;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.Objects;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;

public class TimeValidator {
    private TimeValidator() {}

    private static final class InstanceHolder {
        private static final TimeValidator INSTANCE = new TimeValidator();
    }

    public static TimeValidator getInstance() {
        return TimeValidator.InstanceHolder.INSTANCE;
    }

    public void validateTimeFormat(Long time) throws InvalidFormatException {
        if (Objects.equals(time.intValue(), EMPTY_TIME.getValue()))
            throw new InvalidFormatException(ErrorCode.INVALID_TIME_FORMAT);
    }
}
