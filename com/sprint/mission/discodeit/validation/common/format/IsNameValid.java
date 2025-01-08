package sprint.mission.discodeit.validation.common.format;

import sprint.mission.discodeit.exception.ErrorCode;
import sprint.mission.discodeit.exception.InvalidFormatException;
import static sprint.mission.discodeit.constant.Constants.*;

public class IsNameValid {
    private static final class InstanceHolder {
        private static final IsNameValid INSTANCE = new IsNameValid();
    }

    public static IsNameValid getInstance() {
        return IsNameValid.InstanceHolder.INSTANCE;
    }

    public void validateFormat(String name) throws InvalidFormatException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.ERROR_010);

        if (name.length() > MAX_NAME_LENGTH.getAsInteger())
            throw new InvalidFormatException(ErrorCode.ERROR_010);
    }
}
