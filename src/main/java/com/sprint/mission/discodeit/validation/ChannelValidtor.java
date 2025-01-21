package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;

import java.util.Map;
import java.util.UUID;

public class ChannelValidtor {

    public boolean isUniqueName(String name, Map<UUID, Channel> channels) {
        if (channels.values().stream().anyMatch(channel -> channel.getName().equals(name))) {
            throw new CustomException(ExceptionText.DUPLICATE_NAME);
        }
        return true;
    }
}
