package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record
ChannelDTO(UUID id, String name, String description, Instant createdAt, Instant updatedAt) {}
