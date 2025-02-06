package com.sprint.mission.discodeit.dto;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(UUID id, Instant createdAt, UUID domainId, File file) {
}
