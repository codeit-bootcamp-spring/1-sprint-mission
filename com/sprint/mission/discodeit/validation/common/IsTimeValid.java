package sprint.mission.discodeit.validation.common;

import sprint.mission.discodeit.exception.ErrorCode;
import sprint.mission.discodeit.exception.InvalidIdException;
import sprint.mission.discodeit.exception.InvalidFormatException;

import static sprint.mission.discodeit.constant.Constants.*;

public class IsTimeValid {
    private IsTimeValid() {}

    private static final class InstanceHolder {
        private static final IsTimeValid INSTANCE = new IsTimeValid();
    }

    public static IsTimeValid getInstance() {
        return IsTimeValid.InstanceHolder.INSTANCE;
    }

    public void validateNonEmpty(Long time) throws InvalidFormatException {
        if (time == EMPTY_TIME.getAsInteger())
            throw new InvalidIdException(ErrorCode.ERROR_001);
    }
}
