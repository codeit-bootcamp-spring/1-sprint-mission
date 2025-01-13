package com.sprint.mission.discodeit.validation.format;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;

import static com.sprint.mission.discodeit.constant.IntegerConstant.PHONE_NUMBER_LENGTH;

public class PhoneNumberValidator {
    public void validatePhoneNumberFormat(String phoneNumber) throws InvalidFormatException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);

        if (phoneNumber.replaceAll("-", "").length() !=
                PHONE_NUMBER_LENGTH.getValue())
            throw new InvalidFormatException(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);
    }
}
