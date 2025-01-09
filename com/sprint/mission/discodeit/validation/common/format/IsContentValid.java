package sprint.mission.discodeit.validation.common.format;

import sprint.mission.discodeit.exception.ErrorCode;
import sprint.mission.discodeit.exception.InvalidFormatException;
import static sprint.mission.discodeit.constant.Constants.*;

public class IsContentValid {
    private IsContentValid() {}

    private static final class InstanceHolder {
        private static final IsContentValid INSTANCE = new IsContentValid();
    }

    public static IsContentValid getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateFormat(String content) throws InvalidFormatException {
        if (content == null || content.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.ERROR_020);

        if (content.length() > MAX_CONTENT_LENGTH.getAsInteger())
            throw new InvalidFormatException(ErrorCode.ERROR_020);
    }
}
