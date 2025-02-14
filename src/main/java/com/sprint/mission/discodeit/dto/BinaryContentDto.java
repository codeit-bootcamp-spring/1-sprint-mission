package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.constant.BinaryContentType;
import java.util.UUID;

public record BinaryContentDto(
    UUID uploadedById,
    BinaryContentType type
) {
}
