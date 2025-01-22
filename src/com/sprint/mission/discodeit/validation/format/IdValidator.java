package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

public class IdValidator {
    public void validateIdFormat(UUID id) throws InvalidFormatException {
        if (Objects.equals(id, UUID.fromString(EMPTY_UUID.getValue())))
            throw new InvalidFormatException(ErrorCode.INVALID_ID_FORMAT);
    }
}
