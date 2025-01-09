package sprint.mission.discodeit.validation.common;

import sprint.mission.discodeit.entity.BaseEntity;
import sprint.mission.discodeit.exception.InvalidFormatException;

public class IsBaseEntityValid {
    private final IsIdValid   isIdValid;
    private final IsTimeValid isTimeValid;

    private IsBaseEntityValid() {
        isIdValid   = IsIdValid.getInstance();
        isTimeValid = IsTimeValid.getInstance();
    }

    private static final class InstanceHolder {
        private static final IsBaseEntityValid INSTANCE = new IsBaseEntityValid();
    }

    public static IsBaseEntityValid getInstance() {
        return IsBaseEntityValid.InstanceHolder.INSTANCE;
    }

    public void validateNonEmpty(BaseEntity baseEntity) throws InvalidFormatException {
        isIdValid.validateNonEmpty(baseEntity.getId());
        isTimeValid.validateNonEmpty(baseEntity.getCreateAt());
        isTimeValid.validateNonEmpty(baseEntity.getUpdateAt());
    }
}
