package com.sprint.mission.discodeit.dto;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(UUID id, UUID senderId, Instant createdAt, Instant updatedAt, String content, List<File> fileList) {

}
