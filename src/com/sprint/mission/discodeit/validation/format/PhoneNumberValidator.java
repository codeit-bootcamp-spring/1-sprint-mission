package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sprint.mission.discodeit.constant.IntegerConstant.PHONE_NUMBER_LENGTH;

public class PhoneNumberValidator {
    private static final Pattern PATTERN = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");

    public void validatePhoneNumberFormat(String phoneNumber) throws InvalidFormatException {
        Matcher matcher = PATTERN.matcher(phoneNumber);
        if (!matcher.matches())
            throw new InvalidFormatException(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);
    }
}
