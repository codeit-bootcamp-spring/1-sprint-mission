package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.UUID;

public record ChannelPublicRequest(
    UUID ownerId,
    String name,
    String description
) {

}
