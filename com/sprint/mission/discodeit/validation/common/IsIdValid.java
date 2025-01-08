package sprint.mission.discodeit.validation.common;

import sprint.mission.discodeit.exception.ErrorCode;
import sprint.mission.discodeit.exception.InvalidIdException;
import sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.UUID;

import static sprint.mission.discodeit.constant.Constants.*;

public class IsIdValid {
    private IsIdValid() {}

    private static final class InstanceHolder {
        private static final IsIdValid INSTANCE = new IsIdValid();
    }

    public static IsIdValid getInstance() {
        return IsIdValid.InstanceHolder.INSTANCE;
    }

    public void validateNonEmpty(UUID id) throws InvalidFormatException {
        if (id.equals(UUID.fromString(EMPTY_UUID.getAsString())))
            throw new InvalidIdException(ErrorCode.ERROR_000);
    }
}
