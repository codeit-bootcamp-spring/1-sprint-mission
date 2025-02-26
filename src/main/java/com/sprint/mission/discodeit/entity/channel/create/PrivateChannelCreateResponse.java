package com.sprint.mission.discodeit.entity.channel.create;

import java.time.Instant;
import java.util.UUID;

public record PrivateChannelCreateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String type,
    String name,
    String description
) {

}
