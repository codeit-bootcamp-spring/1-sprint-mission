package com.sprint.mission.discodeit.dto.data;

import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;

public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType,
    BinaryContentUploadStatus uploadStatus
) {

}
