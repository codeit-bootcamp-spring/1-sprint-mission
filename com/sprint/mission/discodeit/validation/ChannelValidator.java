package sprint.mission.discodeit.validation;

import sprint.mission.discodeit.entity.Channel;
import sprint.mission.discodeit.exception.InvalidException;
import sprint.mission.discodeit.validation.common.IsBaseEntityValid;
import sprint.mission.discodeit.validation.common.format.IsNameValid;

public class ChannelValidator implements Validator<Channel> {
    private final IsBaseEntityValid isBaseEntityValid;
    private final IsNameValid       isNameValid;

    private ChannelValidator() {
        isBaseEntityValid = IsBaseEntityValid.getInstance();
        isNameValid       = IsNameValid.getInstance();
    }

    private static final class InstanceHolder {
        private static final ChannelValidator INSTANCE = new ChannelValidator();
    }

    public static ChannelValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void validate(Channel channel) throws InvalidException {
        isBaseEntityValid.validateNonEmpty(channel.getBaseEntity());
        isNameValid.validateFormat(channel.getName());
    }
}
