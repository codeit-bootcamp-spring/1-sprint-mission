package sprint.mission.discodeit.validation.common.format;

import sprint.mission.discodeit.exception.ErrorCode;
import sprint.mission.discodeit.exception.InvalidFormatException;
import static sprint.mission.discodeit.constant.Constants.*;

public class IsPhoneNumberValid {
    private IsPhoneNumberValid() {}

    private static final class InstanceHolder {
        private static final IsPhoneNumberValid INSTANCE = new IsPhoneNumberValid();
    }

    public static IsPhoneNumberValid getInstance() {
        return IsPhoneNumberValid.InstanceHolder.INSTANCE;
    }

    public void validateFormat(String phoneNumber) throws InvalidFormatException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.ERROR_012);

        if (phoneNumber.replaceAll("-", "").length() !=
                PHONE_NUMBER_LENGTH.getAsInteger())
            throw new InvalidFormatException(ErrorCode.ERROR_012);
    }
}
