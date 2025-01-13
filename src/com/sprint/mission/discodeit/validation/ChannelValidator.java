package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.format.NameValidator;

public class ChannelValidator extends BaseEntityValidator {
    private final NameValidator nameValidator;

    private ChannelValidator() {
        nameValidator = new NameValidator();
    }

    private static final class InstanceHolder {
        private static final ChannelValidator INSTANCE = new ChannelValidator();
    }

    public static ChannelValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateBaseEntityFormat(Channel channel) throws InvalidFormatException {
        super.validateIdFormat(channel.getId());
        super.validateCreateAtFormat(channel.getCreateAt());
        super.validateUpdateAtFormat(channel.getUpdateAt());
    }
    public void validateNameFormat(Channel channel) throws InvalidFormatException {
        nameValidator.validateNameFormat(channel.getName());
    }
}
