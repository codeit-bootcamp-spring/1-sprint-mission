package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public record ChannelDto(
        Channel channel,
        List<User> participants,
        List<Message> messages,
        boolean isPublic
) {
}
