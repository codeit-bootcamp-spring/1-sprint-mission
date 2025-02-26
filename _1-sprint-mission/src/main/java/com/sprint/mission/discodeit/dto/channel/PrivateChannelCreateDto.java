package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateDto(
        List<UUID> participantIds
){
}
