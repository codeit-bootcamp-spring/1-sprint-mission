package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.common.BaseEntityValidator;
import com.sprint.mission.discodeit.validation.common.NameValidator;

import static com.sprint.mission.discodeit.constant.IntegerConstant.PHONE_NUMBER_LENGTH;

public class UserValidator {
    private final BaseEntityValidator  baseEntityValidator;
    private final NameValidator        nameValidator;

    private UserValidator() {
        baseEntityValidator  = BaseEntityValidator.getInstance();
        nameValidator        = NameValidator.getInstance();
    }

    private static final class InstanceHolder {
        private static final UserValidator INSTANCE = new UserValidator();
    }

    public static UserValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateBaseEntityFormat(User user) throws InvalidFormatException {
        baseEntityValidator.validateIdFormat(user.getId());
        baseEntityValidator.validateTimeFormat(user.getCreateAt());
        baseEntityValidator.validateTimeFormat(user.getUpdateAt());
    }
    public void validateNameFormat(User user) throws InvalidFormatException {
        nameValidator.validateNameFormat(user.getName());
    }
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
    public void validatePhoneNumberFormat(String phoneNumber) throws InvalidFormatException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);

        if (phoneNumber.replaceAll("-", "").length() !=
                PHONE_NUMBER_LENGTH.getValue())
            throw new InvalidFormatException(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);
    }
}
