package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChannelValidtor {

    public boolean isUniqueName(String name, List<Channel> channels) {
        if (channels.stream().anyMatch(channel -> channel.getName().equals(name))) {
            throw new CustomException(ExceptionText.DUPLICATE_NAME);
        }
        return true;
    }
}
