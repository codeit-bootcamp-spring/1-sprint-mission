package com.sprint.mission.discodeit.entity.channel.create;

import java.time.Instant;
import java.util.UUID;

public record PublicChannelCreateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String type,
    String name,
    String description
) {

}
