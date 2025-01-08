package sprint.mission.discodeit.validation.common;

import sprint.mission.discodeit.entity.BaseEntity;
import sprint.mission.discodeit.exception.ErrorCode;
import sprint.mission.discodeit.exception.InvalidCommonException;
import sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.UUID;

import static sprint.mission.discodeit.constant.Constants.*;

public class IsBaseEntityValid {
    private static final class InstanceHolder {
        private static final IsBaseEntityValid INSTANCE = new IsBaseEntityValid();
    }

    public static IsBaseEntityValid getInstance() {
        return IsBaseEntityValid.InstanceHolder.INSTANCE;
    }

    public void validateNonEmpty(BaseEntity baseEntity) throws InvalidFormatException {
        if (baseEntity.getId().equals(UUID.fromString(EMPTY_UUID.getAsString())))
            throw new InvalidCommonException(ErrorCode.ERROR_000);

        if (baseEntity.getCreateAt() == EMPTY_TIME.getAsInteger())
            throw new InvalidCommonException(ErrorCode.ERROR_001);

        if (baseEntity.getUpdateAt() == EMPTY_TIME.getAsInteger())
            throw new InvalidCommonException(ErrorCode.ERROR_002);
    }
}
