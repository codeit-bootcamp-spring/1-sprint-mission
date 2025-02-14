package com.sprint.mission.discodeit.dto.response.channel;

import java.time.Instant;
import java.util.UUID;

public record
ChannelDTO(UUID id, String name, String description, Instant createdAt, Instant updatedAt) {}
