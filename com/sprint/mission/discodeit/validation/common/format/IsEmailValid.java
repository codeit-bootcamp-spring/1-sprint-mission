package sprint.mission.discodeit.validation.common.format;

import sprint.mission.discodeit.exception.ErrorCode;
import sprint.mission.discodeit.exception.InvalidFormatException;

public class IsEmailValid {
    private static final class InstanceHolder {
        private static final IsEmailValid INSTANCE = new IsEmailValid();
    }

    public static IsEmailValid getInstance() {
        return IsEmailValid.InstanceHolder.INSTANCE;
    }

    public void validateFormat(String email) throws InvalidFormatException {
        if (email == null || email.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.ERROR_011);

        int index;
        if ((index = email.indexOf('@')) == -1)
            throw new InvalidFormatException(ErrorCode.ERROR_011);

        if (email.indexOf('@', index + 1) != -1)
            throw new InvalidFormatException(ErrorCode.ERROR_011);

        if ((index = email.indexOf('.', index + 1)) == -1)
            throw new InvalidFormatException(ErrorCode.ERROR_011);

        if (email.indexOf('.', index + 1) != -1)
            throw new InvalidFormatException(ErrorCode.ERROR_011);
    }
}
