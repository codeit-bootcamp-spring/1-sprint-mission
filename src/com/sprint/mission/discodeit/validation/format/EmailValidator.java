package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

public class EmailValidator {
    public void validateEmailFormat(String email) throws InvalidFormatException {
        if (email == null || email.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.INVALID_EMAIL_FORMAT);

        int index;
        if ((index = email.indexOf('@')) == -1)
            throw new InvalidFormatException(ErrorCode.INVALID_EMAIL_FORMAT);

        if (email.indexOf('@', index + 1) != -1)
            throw new InvalidFormatException(ErrorCode.INVALID_EMAIL_FORMAT);

        if ((index = email.indexOf('.', index + 1)) == -1)
            throw new InvalidFormatException(ErrorCode.INVALID_EMAIL_FORMAT);

        if (email.indexOf('.', index + 1) != -1)
            throw new InvalidFormatException(ErrorCode.INVALID_EMAIL_FORMAT);
    }
}
