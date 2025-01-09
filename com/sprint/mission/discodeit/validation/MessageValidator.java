package sprint.mission.discodeit.validation;

import sprint.mission.discodeit.entity.Message;
import sprint.mission.discodeit.exception.InvalidException;
import sprint.mission.discodeit.validation.common.IsBaseEntityValid;
import sprint.mission.discodeit.validation.common.format.IsContentValid;

public class MessageValidator implements Validator<Message> {
    private final IsBaseEntityValid isBaseEntityValid;
    private final IsContentValid    isContentValid;

    private MessageValidator() {
        isBaseEntityValid = IsBaseEntityValid.getInstance();
        isContentValid    = IsContentValid.getInstance();
    }

    private static final class InstanceHolder {
        private static final MessageValidator INSTANCE = new MessageValidator();
    }

    public static MessageValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void validate(Message message) throws InvalidException {
        isBaseEntityValid.validateNonEmpty(message.getBaseEntity());
        isContentValid.validateFormat(message.getContent());
    }
}
