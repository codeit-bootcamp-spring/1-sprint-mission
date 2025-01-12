package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidEqualityException;
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
        baseEntityValidator.validateTimeFormat(message.getCreateAt());
        baseEntityValidator.validateTimeFormat(message.getUpdateAt());
    }
    public void validateContentFormat(Message message) throws InvalidFormatException {
        String content = message.getContent();
        if (content == null || content.trim().isEmpty())
            throw new InvalidFormatException(ErrorCode.INVALID_CONTENT_FORMAT);

        if (content.length() > MAX_CONTENT_LENGTH.getValue())
            throw new InvalidFormatException(ErrorCode.INVALID_CONTENT_FORMAT);
    }

    public void validateMessageIdEquality(Message message1, Message message2) throws InvalidEqualityException {
        if (!message1.equals(message2))
            throw new InvalidEqualityException(ErrorCode.INVALID_MESSAGE_EQUALITY);
    }
}
