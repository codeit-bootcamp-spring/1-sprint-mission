package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    public void validateEmailFormat(String email) throws InvalidFormatException {
        Matcher matcher = PATTERN.matcher(email);
        if (!matcher.matches())
            throw new InvalidFormatException(ErrorCode.INVALID_EMAIL_FORMAT);
    }
}
