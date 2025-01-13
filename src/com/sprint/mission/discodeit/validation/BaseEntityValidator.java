package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

public abstract class BaseEntityValidator {
    protected BaseEntityValidator() {}

    protected void validateIdFormat(UUID id) throws InvalidFormatException {
        if (Objects.equals(id, UUID.fromString(EMPTY_UUID.getValue())))
            throw new InvalidFormatException(ErrorCode.INVALID_ID_FORMAT);
    }
    protected void validateCreateAtFormat(Long time) throws InvalidFormatException {
        if (Objects.equals(time.intValue(), EMPTY_TIME.getValue()))
            throw new InvalidFormatException(ErrorCode.INVALID_TIME_FORMAT);
    }
    protected void validateUpdateAtFormat(Long time) throws InvalidFormatException {
        if (Objects.equals(time.intValue(), EMPTY_TIME.getValue()))
            throw new InvalidFormatException(ErrorCode.INVALID_TIME_FORMAT);
    }
}
