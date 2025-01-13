package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.format.EmailValidator;
import com.sprint.mission.discodeit.validation.format.NameValidator;
import com.sprint.mission.discodeit.validation.format.PhoneNumberValidator;

public class UserValidator extends BaseEntityValidator {
    private final NameValidator        nameValidator;
    private final PhoneNumberValidator phoneNumberValidator;
    private final EmailValidator       emailValidator;

    private UserValidator() {
        nameValidator        = new NameValidator();
        phoneNumberValidator = new PhoneNumberValidator();
        emailValidator       = new EmailValidator();
    }

    private static final class InstanceHolder {
        private static final UserValidator INSTANCE = new UserValidator();
    }

    public static UserValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateBaseEntityFormat(User user) throws InvalidFormatException {
        super.validateIdFormat(user.getId());
        super.validateCreateAtFormat(user.getCreateAt());
        super.validateUpdateAtFormat(user.getUpdateAt());
    }
    public void validateNameFormat(User user) throws InvalidFormatException {
        nameValidator.validateNameFormat(user.getName());
    }
    public void validateEmailFormat(User user) throws InvalidFormatException {
        emailValidator.validateEmailFormat(user.getEmail());
    }
    public void validatePhoneNumberFormat(User user) throws InvalidFormatException {
        phoneNumberValidator.validatePhoneNumberFormat(user.getPhoneNumber());
    }
}
