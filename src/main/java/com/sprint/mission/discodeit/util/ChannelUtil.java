package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.exception.validation.channel.InvalidChannelNameException;
import com.sprint.mission.discodeit.exception.validation.channel.InvalidChannelTypeException;
import com.sprint.mission.discodeit.exception.validation.channel.InvalidDescriptionException;

import java.util.Arrays;

public class ChannelUtil {
    public static void validChannelId(Channel channel) {
        if (channel.getId() == null) {
            throw new NotfoundIdException("유효하지 않은 id입니다.");
        }
    }

    public static void validChannelName(String name) {
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new InvalidChannelNameException("너무 긴 채널명입니다.");
        }
    }

    public static void validDescription(String description) {
        if (description.length() > 100) {
            throw new InvalidDescriptionException("너무 긴 설명합니다.");
        }
    }

    public static void validChannelType(ChannelType channelType) {
        if (channelType == null || !Arrays.asList(ChannelType.values()).contains(channelType)) {
            throw new InvalidChannelTypeException("유효하지 않은 채널 타입입니다.");
        }
    }

    public static void checkValid(String name, String description, ChannelType channelType) {
        validChannelName(name);
        validDescription(description);
        validChannelType(channelType);
    }
}
