package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.common.BaseEntityValidator;

import static com.sprint.mission.discodeit.constant.IntegerConstant.MAX_CONTENT_LENGTH;

public class MessageValidator {
    private final BaseEntityValidator baseEntityValidator;

    private MessageValidator() {
        baseEntityValidator = BaseEntityValidator.getInstance();
    }

    private static final class InstanceHolder {
        private static final MessageValidator INSTANCE = new MessageValidator();
    }

    public static MessageValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateBaseEntityFormat(Message message) throws InvalidFormatException {
        baseEntityValidator.validateIdFormat(message.getId());
        baseEntityValidator.validateCreateAtFormat(message.getCreateAt());
        baseEntityValidator.validateUpdateAtFormat(message.getUpdateAt());
    }
    public void validateContentFormat(Message message) throws InvalidFormatException {
        String content = message.getContent();
        if (content == null || content.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.INVALID_CONTENT_FORMAT);

        if (content.length() > MAX_CONTENT_LENGTH.getValue())
            throw new InvalidFormatException(ErrorCode.INVALID_CONTENT_FORMAT);
    }
}
