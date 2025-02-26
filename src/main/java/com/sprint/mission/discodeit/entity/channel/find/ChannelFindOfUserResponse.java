package com.sprint.mission.discodeit.entity.channel.find;

import java.util.List;
import java.util.UUID;

public record ChannelFindOfUserResponse(
    UUID id,
    String type,
    String name,
    String description,
    List<UUID> participantIds
) {

}
