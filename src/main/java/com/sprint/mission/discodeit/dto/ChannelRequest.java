package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record ChannelRequest(
) {
    public record CreatePublic(
            String title,
            String description
    ) {}

    public record CreatePrivate(
            List<UUID> joinUsers
    ) {}

    public record Update(
            String title,
            String description
    ) {}
}
