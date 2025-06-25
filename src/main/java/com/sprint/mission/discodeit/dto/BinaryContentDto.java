package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent.UploadStatus;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    Long size,
    String fileName,
    String contentType,
    UploadStatus uploadStatus
) {

}
