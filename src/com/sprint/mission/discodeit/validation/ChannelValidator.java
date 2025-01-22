package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.validation.format.CreateAtValidator;
import com.sprint.mission.discodeit.validation.format.NameValidator;
import com.sprint.mission.discodeit.validation.format.IdValidator;
import com.sprint.mission.discodeit.validation.format.UpdateAtValidator;


public class ChannelValidator {
    private final IdValidator       idValidator;
    private final CreateAtValidator createAtValidator;
    private final UpdateAtValidator updateAtValidator;
    private final NameValidator     nameValidator;

    private ChannelValidator() {
        idValidator       = new IdValidator();
        createAtValidator = new CreateAtValidator();
        updateAtValidator = new UpdateAtValidator();
        nameValidator     = new NameValidator();
    }

    private static final class InstanceHolder {
        private static final ChannelValidator INSTANCE = new ChannelValidator();
    }
    public static ChannelValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateIdFormat(Channel channel) {
        idValidator.validateIdFormat(channel.getId());
    }
    public void validateCreateAtFormat(Channel channel) {
        createAtValidator.validateCreateAtFormat(channel.getCreateAt());
    }
    public void validateUpdateAtFormat(Channel channel) throws InvalidFormatException {
        updateAtValidator.validateUpdateAtFormat(channel.getUpdateAt());
    }
    public void validateNameFormat(Channel channel) throws InvalidFormatException {
        nameValidator.validateNameFormat(channel.getName());
    }
}
