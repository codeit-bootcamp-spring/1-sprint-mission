package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.format.ContentValidator;
import com.sprint.mission.discodeit.validation.format.CreateAtValidator;
import com.sprint.mission.discodeit.validation.format.IdValidator;
import com.sprint.mission.discodeit.validation.format.UpdateAtValidator;

public class MessageValidator {
    private final IdValidator       idValidator;
    private final CreateAtValidator createAtValidator;
    private final UpdateAtValidator updateAtValidator;
    private final ContentValidator  contentValidator;

    private MessageValidator() {
        idValidator       = new IdValidator();
        createAtValidator = new CreateAtValidator();
        updateAtValidator = new UpdateAtValidator();
        contentValidator  = new ContentValidator();
    }

    private static final class InstanceHolder {
        private static final MessageValidator INSTANCE = new MessageValidator();
    }
    public static MessageValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateIdFormat(Message message) throws InvalidFormatException {
        idValidator.validateIdFormat(message.getId());
    }
    public void validateCreateAtFormat(Message message) throws InvalidFormatException {
        createAtValidator.validateCreateAtFormat(message.getCreateAt());
    }
    public void validateUpdateAtFormat(Message message) throws InvalidFormatException {
        updateAtValidator.validateUpdateAtFormat(message.getUpdateAt());
    }
    public void validateContentFormat(Message message) throws InvalidFormatException {
        contentValidator.validateContentFormat(message.getContent());
    }
}
