package com.sprint.mission.discodeit.service.validate;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;

import java.util.Optional;

public class ChannelServiceValidator implements ServiceValidator<Channel> {
    @Override
    public Channel entityValidate(Channel channel) {
        return Optional.ofNullable(channel)
                        .orElseThrow(ChannelNotFoundException::new);
    }

}
