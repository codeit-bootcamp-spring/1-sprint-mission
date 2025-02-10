package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import static com.sprint.mission.discodeit.constant.IntegerConstant.MAX_CONTENT_LENGTH;

public class ContentValidator {
    public void validateContentFormat(String content) throws InvalidFormatException {
        if (content == null || content.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.INVALID_CONTENT_FORMAT);

        if (content.length() > MAX_CONTENT_LENGTH.getValue())
            throw new InvalidFormatException(ErrorCode.INVALID_CONTENT_FORMAT);
    }
}
