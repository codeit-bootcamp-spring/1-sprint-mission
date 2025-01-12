package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.InvalidEqualityException;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.common.BaseEntityValidator;
import com.sprint.mission.discodeit.validation.common.NameValidator;

public class ChannelValidator {
    private final BaseEntityValidator baseEntityValidator;
    private final NameValidator       nameValidator;

    private ChannelValidator() {
        baseEntityValidator = BaseEntityValidator.getInstance();
        nameValidator       = NameValidator.getInstance();
    }

    private static final class InstanceHolder {
        private static final ChannelValidator INSTANCE = new ChannelValidator();
    }

    public static ChannelValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateBaseEntityFormat(Channel channel) throws InvalidFormatException {
        baseEntityValidator.validateIdFormat(channel.getId());
        baseEntityValidator.validateTimeFormat(channel.getCreateAt());
        baseEntityValidator.validateTimeFormat(channel.getUpdateAt());
    }
    public void validateNameFormat(Channel channel) throws InvalidFormatException {
        nameValidator.validateNameFormat(channel.getName());
    }

    public void validateChannelIdEquality(Channel channel1, Channel channel2) throws InvalidEqualityException {
        if (!channel1.equals(channel2))
            throw new InvalidEqualityException(ErrorCode.INVALID_CHANNEL_EQUALITY);
    }
}
