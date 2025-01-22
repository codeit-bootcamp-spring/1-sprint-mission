package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.format.*;

public class UserValidator {
    private final IdValidator          idValidator;
    private final CreateAtValidator    createAtValidator;
    private final UpdateAtValidator    updateAtValidator;
    private final NameValidator        nameValidator;
    private final EmailValidator       emailValidator;
    private final PhoneNumberValidator phoneNumberValidator;

    private UserValidator() {
        idValidator          = new IdValidator();
        createAtValidator    = new CreateAtValidator();
        updateAtValidator    = new UpdateAtValidator();
        nameValidator        = new NameValidator();
        emailValidator       = new EmailValidator();
        phoneNumberValidator = new PhoneNumberValidator();
    }

    private static final class InstanceHolder {
        private static final UserValidator INSTANCE = new UserValidator();
    }
    public static UserValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateIdFormat(User user) throws InvalidFormatException {
        idValidator.validateIdFormat(user.getId());
    }
    public void validateCreateAtFormat(User user) {
        createAtValidator.validateCreateAtFormat(user.getCreateAt());
    }
    public void validateUpdateAtFormat(User user) throws InvalidFormatException {
        updateAtValidator.validateUpdateAtFormat(user.getUpdateAt());
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
