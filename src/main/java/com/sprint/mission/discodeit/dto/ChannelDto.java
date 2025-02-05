package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record ChannelDto(UUID id, String name, Instant createdAt, Instant updatedAt) {
}