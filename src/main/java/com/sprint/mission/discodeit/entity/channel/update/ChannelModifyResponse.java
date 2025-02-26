package com.sprint.mission.discodeit.entity.channel.update;

import java.time.Instant;
import java.util.UUID;

public record ChannelModifyResponse(
    UUID id,
    Instant createdAt,
    Instant updateAt,
    String type,
    String name,
    String description
) {

}
