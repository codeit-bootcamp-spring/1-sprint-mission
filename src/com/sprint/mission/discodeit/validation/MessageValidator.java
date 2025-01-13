package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.format.ContentValidator;

public class MessageValidator extends BaseEntityValidator {
    private final ContentValidator contentValidator;

    private MessageValidator() {
        contentValidator = new ContentValidator();
    }

    private static final class InstanceHolder {
        private static final MessageValidator INSTANCE = new MessageValidator();
    }

    public static MessageValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateBaseEntityFormat(Message message) throws InvalidFormatException {
        super.validateIdFormat(message.getId());
        super.validateCreateAtFormat(message.getCreateAt());
        super.validateUpdateAtFormat(message.getUpdateAt());
    }
    public void validateContentFormat(Message message) throws InvalidFormatException {
        contentValidator.validateContentFormat(message.getContent());
    }
}
