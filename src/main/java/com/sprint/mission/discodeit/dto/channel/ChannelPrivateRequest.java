package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record ChannelPrivateRequest (
        ChannelType type,
        UUID ownerId
){
}
