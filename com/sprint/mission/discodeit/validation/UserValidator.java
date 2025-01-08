package sprint.mission.discodeit.validation;

import sprint.mission.discodeit.entity.User;
import sprint.mission.discodeit.exception.InvalidException;
import sprint.mission.discodeit.validation.common.IsBaseEntityValid;
import sprint.mission.discodeit.validation.common.format.IsEmailValid;
import sprint.mission.discodeit.validation.common.format.IsNameValid;
import sprint.mission.discodeit.validation.common.format.IsPhoneNumberValid;

public class UserValidator implements Validator<User> {
    private final IsBaseEntityValid  isBaseEntityValid;
    private final IsNameValid        isNameValid;
    private final IsEmailValid       isEmailValid;
    private final IsPhoneNumberValid isPhoneNumberValid;

    private UserValidator() {
        isBaseEntityValid  = IsBaseEntityValid.getInstance();
        isNameValid        = IsNameValid.getInstance();
        isEmailValid       = IsEmailValid.getInstance();
        isPhoneNumberValid = IsPhoneNumberValid.getInstance();
    }

    private static final class InstanceHolder {
        private static final UserValidator INSTANCE = new UserValidator();
    }

    public static UserValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void validate(User user) throws InvalidException {
        isBaseEntityValid.validateNonEmpty(user.getCommon());
        isNameValid.validateFormat(user.getName());
        isEmailValid.validateFormat(user.getEmail());
        // isPhoneNumberValid.validateFormat(user.getPhoneNumber()); // option
    }
}
